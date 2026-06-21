package com.armada.expiryapp.ui.screens.ocr

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.OcrDateParser
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

// ── State machine ─────────────────────────────────────────────────────────────

private sealed class OcrState {
    object Scanning : OcrState()
    data class Found(
        val date:       LocalDate,
        val displayStr: String,
        val rawDigits:  String,
        val confidence: OcrDateParser.Confidence,
    ) : OcrState()
    data class Multiple(val dates: List<LocalDate>) : OcrState()
    object NotFound : OcrState()
}

// ── Public entry point ────────────────────────────────────────────────────────

@Composable
fun OcrScannerScreen(
    onDateDetected:  (String) -> Unit,
    onEnterManually: () -> Unit,
    onBack:          () -> Unit,
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    var permissionDenied by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        permissionDenied    = !granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    when {
        hasCameraPermission -> OcrContent(
            onDateDetected  = onDateDetected,
            onEnterManually = onEnterManually,
            onBack          = onBack,
        )
        permissionDenied    -> OcrPermissionDenied(onBack = onBack)
        else                -> Box(
            Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center,
        ) { Text("Requesting camera permission…", color = Color.White) }
    }
}

// ── Camera + OCR content ──────────────────────────────────────────────────────

@Composable
private fun OcrContent(
    onDateDetected:  (String) -> Unit,
    onEnterManually: () -> Unit,
    onBack:          () -> Unit,
) {
    val context        = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var ocrState    by remember { mutableStateOf<OcrState>(OcrState.Scanning) }
    val isScanActive = remember { AtomicBoolean(true) }
    val executor     = remember { Executors.newSingleThreadExecutor() }
    val recognizer   = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    val previewView  = remember { PreviewView(context) }

    // ── Camera lifecycle ──────────────────────────────────────────────────────

    DisposableEffect(lifecycleOwner) {
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener({
            val provider = future.get()
            val preview  = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { ia ->
                    ia.setAnalyzer(executor) { proxy ->
                        processOcrFrame(proxy, recognizer, isScanActive) { newState ->
                            ocrState = newState
                        }
                    }
                }
            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis,
                )
            } catch (_: Exception) {}
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            runCatching { future.get().unbindAll() }
            recognizer.close()
            executor.shutdown()
        }
    }

    // ── Timeout + high-confidence auto-accept ──────────────────────────────────

    LaunchedEffect(ocrState) {
        when (val s = ocrState) {
            is OcrState.Scanning -> {
                delay(10_000L)
                if (ocrState is OcrState.Scanning) {
                    isScanActive.set(false)
                    ocrState = OcrState.NotFound
                }
            }
            is OcrState.Found -> {
                if (s.confidence == OcrDateParser.Confidence.HIGH) {
                    delay(700L)
                    onDateDetected(s.rawDigits)
                }
            }
            else -> {}
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    Box(modifier = Modifier.fillMaxSize()) {

        // Camera feed
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Scan zone overlay
        OcrScanOverlay(modifier = Modifier.fillMaxSize())

        // Back button
        IconButton(
            onClick  = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 8.dp)
                .size(48.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        // State-driven bottom panel
        val tryAgain: () -> Unit = {
            isScanActive.set(true)
            ocrState = OcrState.Scanning
        }

        Box(
            modifier         = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
        ) {
            when (val s = ocrState) {
                is OcrState.Scanning ->
                    ScanningHint()

                is OcrState.Found ->
                    if (s.confidence == OcrDateParser.Confidence.HIGH) {
                        HighConfidenceFlash(displayStr = s.displayStr)
                    } else {
                        ResultCard(
                            displayStr      = s.displayStr,
                            onAccept        = { onDateDetected(s.rawDigits) },
                            onTryAgain      = tryAgain,
                            onEnterManually = onEnterManually,
                        )
                    }

                is OcrState.Multiple ->
                    MultipleCard(
                        dates           = s.dates,
                        onSelect        = { date ->
                            onDateDetected(OcrDateParser.toRawDigits(date))
                        },
                        onTryAgain      = tryAgain,
                        onEnterManually = onEnterManually,
                    )

                is OcrState.NotFound ->
                    NotFoundCard(
                        onTryAgain      = tryAgain,
                        onEnterManually = onEnterManually,
                    )
            }
        }
    }
}

// ── Frame processing ──────────────────────────────────────────────────────────

private fun processOcrFrame(
    imageProxy:  ImageProxy,
    recognizer:  TextRecognizer,
    isScanActive: AtomicBoolean,
    onStateUpdate: (OcrState) -> Unit,
) {
    if (!isScanActive.get()) { imageProxy.close(); return }

    val bitmap = runCatching { imageProxy.toBitmap() }.getOrNull()
    if (bitmap == null) { imageProxy.close(); return }

    val processed = applyPreProcessing(bitmap)
    val image     = InputImage.fromBitmap(processed, imageProxy.imageInfo.rotationDegrees)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            if (!isScanActive.get()) return@addOnSuccessListener

            val rawText = visionText.textBlocks
                .flatMap { it.lines }
                .joinToString(" ") { it.text }

            val dates = OcrDateParser.parseAll(rawText)
            if (dates.isEmpty()) return@addOnSuccessListener

            isScanActive.set(false)

            val uniqueDates = dates.map { it.date }.distinct()
            if (uniqueDates.size == 1) {
                val pd   = dates.first()
                val conf = if (dates.all { it.confidence == OcrDateParser.Confidence.HIGH })
                    OcrDateParser.Confidence.HIGH else OcrDateParser.Confidence.MEDIUM
                onStateUpdate(
                    OcrState.Found(
                        date       = pd.date,
                        displayStr = OcrDateParser.toDisplayString(pd.date),
                        rawDigits  = OcrDateParser.toRawDigits(pd.date),
                        confidence = conf,
                    )
                )
            } else {
                onStateUpdate(OcrState.Multiple(uniqueDates.take(5)))
            }
        }
        .addOnCompleteListener {
            processed.recycle()
            imageProxy.close()
        }
}

// Pre-processing: grayscale + contrast enhancement (improves OCR on faded/small text)
private fun applyPreProcessing(source: Bitmap): Bitmap {
    val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(result)
    val paint  = Paint(Paint.FILTER_BITMAP_FLAG)

    val matrix    = ColorMatrix()
    matrix.setSaturation(0f)                                        // grayscale

    val scale     = 1.5f
    val translate = (-0.5f * scale + 0.5f) * 255f
    val contrast  = ColorMatrix(floatArrayOf(
        scale, 0f,    0f,    0f, translate,
        0f,    scale, 0f,    0f, translate,
        0f,    0f,    scale, 0f, translate,
        0f,    0f,    0f,    1f, 0f,
    ))
    matrix.postConcat(contrast)                                     // contrast boost
    paint.colorFilter = ColorMatrixColorFilter(matrix)
    canvas.drawBitmap(source, 0f, 0f, paint)
    source.recycle()
    return result
}

// ── Scan zone overlay ─────────────────────────────────────────────────────────

@Composable
private fun OcrScanOverlay(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w     = size.width
        val h     = size.height
        val rW    = w * 0.85f
        val rH    = rW * 0.55f          // taller than barcode scanner — captures more text
        val left  = (w - rW) / 2f
        val top   = (h - rH) / 2f - h * 0.04f
        val dark  = Color.Black.copy(alpha = 0.5f)

        drawRect(dark, Offset.Zero, Size(w, top))
        drawRect(dark, Offset(0f, top + rH), Size(w, h - top - rH))
        drawRect(dark, Offset(0f, top), Size(left, rH))
        drawRect(dark, Offset(left + rW, top), Size(w - left - rW, rH))

        drawRoundRect(
            color        = Color.White,
            topLeft      = Offset(left, top),
            size         = Size(rW, rH),
            cornerRadius = CornerRadius(10.dp.toPx()),
            style        = Stroke(2.dp.toPx()),
        )

        val cLen = 20.dp.toPx()
        val cW   = 3.dp.toPx()
        val r    = 10.dp.toPx()
        val acc  = ArmadaColors.FieldActiveBorder
        drawLine(acc, Offset(left + r, top),             Offset(left + r + cLen, top),           cW)
        drawLine(acc, Offset(left, top + r),             Offset(left, top + r + cLen),           cW)
        drawLine(acc, Offset(left + rW - r - cLen, top), Offset(left + rW - r, top),             cW)
        drawLine(acc, Offset(left + rW, top + r),        Offset(left + rW, top + r + cLen),      cW)
        drawLine(acc, Offset(left + r, top + rH),        Offset(left + r + cLen, top + rH),      cW)
        drawLine(acc, Offset(left, top + rH - r - cLen), Offset(left, top + rH - r),             cW)
        drawLine(acc, Offset(left + rW - r - cLen, top + rH), Offset(left + rW - r, top + rH),  cW)
        drawLine(acc, Offset(left + rW, top + rH - r - cLen), Offset(left + rW, top + rH - r),  cW)
    }
}

// ── State UI composables ──────────────────────────────────────────────────────

@Composable
private fun ScanningHint(modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Point camera at the expiry date",
            style      = MaterialTheme.typography.bodyLarge,
            color      = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign  = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Keep the date inside the frame",
            style     = MaterialTheme.typography.bodySmall,
            color     = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HighConfidenceFlash(
    displayStr: String,
    modifier:   Modifier = Modifier,
) {
    Box(
        modifier         = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(Color(0xCC27AE60), RoundedCornerShape(12.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✅", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(4.dp))
            Text(
                text       = displayStr,
                style      = MaterialTheme.typography.titleLarge,
                color      = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ResultCard(
    displayStr:      String,
    onAccept:        () -> Unit,
    onTryAgain:      () -> Unit,
    onEnterManually: () -> Unit,
    modifier:        Modifier = Modifier,
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "🟡  Is this correct?",
                style     = MaterialTheme.typography.labelMedium,
                color     = ArmadaColors.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                displayStr,
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color      = ArmadaColors.BrandTitle,
                textAlign  = TextAlign.Center,
            )
            Spacer(Modifier.height(14.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick  = onAccept,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.buttonColors(containerColor = ArmadaColors.BtnNewEntryReady),
                    shape    = RoundedCornerShape(8.dp),
                ) { Text("✅ Accept", color = Color.White) }

                OutlinedButton(
                    onClick  = onTryAgain,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(8.dp),
                ) { Text("🔄 Try Again") }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "✏️  Enter Manually",
                style    = MaterialTheme.typography.labelMedium,
                color    = ArmadaColors.BrandAccent,
                modifier = Modifier.clickable(onClick = onEnterManually),
            )
        }
    }
}

@Composable
private fun MultipleCard(
    dates:           List<LocalDate>,
    onSelect:        (LocalDate) -> Unit,
    onTryAgain:      () -> Unit,
    onEnterManually: () -> Unit,
    modifier:        Modifier = Modifier,
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Multiple dates found — tap to select:",
                style     = MaterialTheme.typography.labelMedium,
                color     = ArmadaColors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(10.dp))
            dates.forEachIndexed { idx, date ->
                Text(
                    text      = OcrDateParser.toDisplayString(date),
                    style     = MaterialTheme.typography.bodyLarge,
                    color     = ArmadaColors.BrandAccent,
                    fontWeight = FontWeight.Medium,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(date) }
                        .padding(vertical = 8.dp),
                )
                if (idx < dates.lastIndex) {
                    HorizontalDivider(color = ArmadaColors.Border, thickness = 0.5.dp)
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick  = onTryAgain,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(8.dp),
                ) { Text("🔄 Try Again") }
                OutlinedButton(
                    onClick  = onEnterManually,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(8.dp),
                ) { Text("✏️ Manual") }
            }
        }
    }
}

@Composable
private fun NotFoundCard(
    onTryAgain:      () -> Unit,
    onEnterManually: () -> Unit,
    modifier:        Modifier = Modifier,
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "🔴  Could not read expiry date",
                style      = MaterialTheme.typography.bodyMedium,
                color      = ArmadaColors.TextPrimary,
                fontWeight = FontWeight.Medium,
                textAlign  = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Try better lighting or enter the date manually.",
                style     = MaterialTheme.typography.bodySmall,
                color     = ArmadaColors.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(14.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick  = onTryAgain,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(8.dp),
                ) { Text("🔄 Try Again") }
                Button(
                    onClick  = onEnterManually,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.buttonColors(containerColor = ArmadaColors.BrandAccent),
                    shape    = RoundedCornerShape(8.dp),
                ) { Text("✏️ Manual", color = Color.White) }
            }
        }
    }
}

// ── Permission denied ─────────────────────────────────────────────────────────

@Composable
private fun OcrPermissionDenied(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier         = modifier.fillMaxSize().background(ArmadaColors.BgApp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier            = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Camera permission required",
                style      = MaterialTheme.typography.titleMedium,
                color      = ArmadaColors.BrandTitle,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Camera permission is required to scan expiry dates. Please enable it in Settings → Apps → ArmadaExpiryApp → Permissions.",
                style     = MaterialTheme.typography.bodyMedium,
                color     = ArmadaColors.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onBack) { Text("Go Back") }
        }
    }
}
