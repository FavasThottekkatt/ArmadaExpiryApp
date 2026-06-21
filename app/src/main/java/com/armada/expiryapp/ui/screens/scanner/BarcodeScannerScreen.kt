package com.armada.expiryapp.ui.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.ScanFeedback
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@Composable
fun BarcodeScannerScreen(
    onBarcodeDetected: (String) -> Unit,
    onBack:            () -> Unit,
) {
    val context        = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val view           = LocalView.current

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
        hasCameraPermission -> CameraPreview(
            lifecycleOwner     = lifecycleOwner,
            view               = view,
            onBarcodeDetected  = onBarcodeDetected,
            onBack             = onBack,
        )

        permissionDenied -> PermissionDeniedContent(onBack = onBack)

        else -> Box(
            modifier           = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment   = Alignment.Center,
        ) {
            Text("Requesting camera permission…", color = Color.White)
        }
    }
}

// ── Camera preview + scan overlay ─────────────────────────────────────────────

@Composable
private fun CameraPreview(
    lifecycleOwner:    androidx.lifecycle.LifecycleOwner,
    view:              android.view.View,
    onBarcodeDetected: (String) -> Unit,
    onBack:            () -> Unit,
) {
    val context    = LocalContext.current
    val scanned    = remember { AtomicBoolean(false) }
    val executor   = remember { Executors.newSingleThreadExecutor() }

    val barcodeScanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_UPC_A,
                    Barcode.FORMAT_UPC_E,
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_CODE_39,
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_DATA_MATRIX,
                )
                .build()
        )
    }

    val previewView = remember { PreviewView(context) }

    DisposableEffect(lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(executor) { imageProxy ->
                        processFrame(
                            imageProxy     = imageProxy,
                            barcodeScanner = barcodeScanner,
                            scanned        = scanned,
                            onDetected     = { barcode ->
                                ScanFeedback.play(view)
                                onBarcodeDetected(barcode)
                            },
                        )
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis,
                )
            } catch (_: Exception) {}
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            runCatching { cameraProviderFuture.get().unbindAll() }
            barcodeScanner.close()
            executor.shutdown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory  = { previewView },
            modifier = Modifier.fillMaxSize(),
        )

        ScanOverlay(modifier = Modifier.fillMaxSize())

        // Back button
        IconButton(
            onClick  = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 8.dp)
                .size(48.dp),
        ) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint               = Color.White,
            )
        }

        // Instruction text
        Column(
            modifier            = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text       = "Point camera at barcode",
                style      = MaterialTheme.typography.bodyLarge,
                color      = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign  = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = "Scanning automatically…",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun processFrame(
    imageProxy:     ImageProxy,
    barcodeScanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    scanned:        AtomicBoolean,
    onDetected:     (String) -> Unit,
) {
    if (scanned.get()) { imageProxy.close(); return }

    val mediaImage = imageProxy.image
    if (mediaImage == null) { imageProxy.close(); return }

    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    barcodeScanner.process(image)
        .addOnSuccessListener { barcodes ->
            val value = barcodes.firstOrNull()?.rawValue
            if (!value.isNullOrBlank() && scanned.compareAndSet(false, true)) {
                onDetected(value)
            }
        }
        .addOnCompleteListener { imageProxy.close() }
}

// ── Scan zone overlay ──────────────────────────────────────────────────────────

@Composable
private fun ScanOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val canvasW = size.width
        val canvasH = size.height

        val rectW  = canvasW * 0.75f
        val rectH  = rectW * 0.45f
        val left   = (canvasW - rectW) / 2f
        val top    = (canvasH - rectH) / 2f - canvasH * 0.05f

        // Dark overlay — four quadrants around the scan zone
        val overlayColor = Color.Black.copy(alpha = 0.55f)

        // Top
        drawRect(color = overlayColor, topLeft = Offset(0f, 0f), size = Size(canvasW, top))
        // Bottom
        drawRect(color = overlayColor, topLeft = Offset(0f, top + rectH), size = Size(canvasW, canvasH - top - rectH))
        // Left
        drawRect(color = overlayColor, topLeft = Offset(0f, top), size = Size(left, rectH))
        // Right
        drawRect(color = overlayColor, topLeft = Offset(left + rectW, top), size = Size(canvasW - left - rectW, rectH))

        // Scan zone border
        drawRoundRect(
            color       = Color.White,
            topLeft     = Offset(left, top),
            size        = Size(rectW, rectH),
            cornerRadius = CornerRadius(12.dp.toPx()),
            style       = Stroke(width = 2.dp.toPx()),
        )

        // Corner accent marks
        val cornerLen = 24.dp.toPx()
        val cornerW   = 3.dp.toPx()
        val r         = 12.dp.toPx()
        val accentColor = ArmadaColors.FieldActiveBorder

        // Top-left
        drawLine(accentColor, Offset(left + r, top), Offset(left + r + cornerLen, top), cornerW)
        drawLine(accentColor, Offset(left, top + r), Offset(left, top + r + cornerLen), cornerW)
        // Top-right
        drawLine(accentColor, Offset(left + rectW - r - cornerLen, top), Offset(left + rectW - r, top), cornerW)
        drawLine(accentColor, Offset(left + rectW, top + r), Offset(left + rectW, top + r + cornerLen), cornerW)
        // Bottom-left
        drawLine(accentColor, Offset(left + r, top + rectH), Offset(left + r + cornerLen, top + rectH), cornerW)
        drawLine(accentColor, Offset(left, top + rectH - r - cornerLen), Offset(left, top + rectH - r), cornerW)
        // Bottom-right
        drawLine(accentColor, Offset(left + rectW - r - cornerLen, top + rectH), Offset(left + rectW - r, top + rectH), cornerW)
        drawLine(accentColor, Offset(left + rectW, top + rectH - r - cornerLen), Offset(left + rectW, top + rectH - r), cornerW)
    }
}

// ── Permission denied state ────────────────────────────────────────────────────

@Composable
private fun PermissionDeniedContent(onBack: () -> Unit) {
    Box(
        modifier         = Modifier.fillMaxSize().background(ArmadaColors.BgApp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(32.dp),
        ) {
            Text(
                text       = "Camera permission required",
                style      = MaterialTheme.typography.titleMedium,
                color      = ArmadaColors.BrandTitle,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text      = "Camera permission is required to scan barcodes. Please enable it in Settings → Apps → ArmadaExpiryApp → Permissions.",
                style     = MaterialTheme.typography.bodyMedium,
                color     = ArmadaColors.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            androidx.compose.material3.Button(onClick = onBack) {
                Text("Go Back")
            }
        }
    }
}
