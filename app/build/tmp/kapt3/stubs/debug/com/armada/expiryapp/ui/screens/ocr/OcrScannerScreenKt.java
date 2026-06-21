package com.armada.expiryapp.ui.screens.ocr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.core.content.ContextCompat;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import com.armada.expiryapp.util.OcrDateParser;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000N\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u001a8\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0007\u001a8\u0010\b\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0003\u001a4\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\u0003H\u0002\u001a\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0002\u001a\u0012\u0010\u0015\u001a\u00020\u00012\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001a\u0012\u0010\u0018\u001a\u00020\u00012\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001a\u001a\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001aD\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00042\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001aP\u0010\u001e\u001a\u00020\u00012\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 2\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001a.\u0010#\u001a\u00020\u00012\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001a \u0010$\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u00a8\u0006%"}, d2 = {"OcrScannerScreen", "", "onDateDetected", "Lkotlin/Function1;", "", "onEnterManually", "Lkotlin/Function0;", "onBack", "OcrContent", "processOcrFrame", "imageProxy", "Landroidx/camera/core/ImageProxy;", "recognizer", "Lcom/google/mlkit/vision/text/TextRecognizer;", "isScanActive", "Ljava/util/concurrent/atomic/AtomicBoolean;", "onStateUpdate", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState;", "applyPreProcessing", "Landroid/graphics/Bitmap;", "source", "OcrScanOverlay", "modifier", "Landroidx/compose/ui/Modifier;", "ScanningHint", "HighConfidenceFlash", "displayStr", "ResultCard", "onAccept", "onTryAgain", "MultipleCard", "dates", "", "Ljava/time/LocalDate;", "onSelect", "NotFoundCard", "OcrPermissionDenied", "app_debug"})
public final class OcrScannerScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void OcrScannerScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDateDetected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEnterManually, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OcrContent(kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDateDetected, kotlin.jvm.functions.Function0<kotlin.Unit> onEnterManually, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    private static final void processOcrFrame(androidx.camera.core.ImageProxy imageProxy, com.google.mlkit.vision.text.TextRecognizer recognizer, java.util.concurrent.atomic.AtomicBoolean isScanActive, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.ui.screens.ocr.OcrState, kotlin.Unit> onStateUpdate) {
    }
    
    private static final android.graphics.Bitmap applyPreProcessing(android.graphics.Bitmap source) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OcrScanOverlay(androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ScanningHint(androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HighConfidenceFlash(java.lang.String displayStr, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResultCard(java.lang.String displayStr, kotlin.jvm.functions.Function0<kotlin.Unit> onAccept, kotlin.jvm.functions.Function0<kotlin.Unit> onTryAgain, kotlin.jvm.functions.Function0<kotlin.Unit> onEnterManually, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MultipleCard(java.util.List<java.time.LocalDate> dates, kotlin.jvm.functions.Function1<? super java.time.LocalDate, kotlin.Unit> onSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onTryAgain, kotlin.jvm.functions.Function0<kotlin.Unit> onEnterManually, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NotFoundCard(kotlin.jvm.functions.Function0<kotlin.Unit> onTryAgain, kotlin.jvm.functions.Function0<kotlin.Unit> onEnterManually, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OcrPermissionDenied(kotlin.jvm.functions.Function0<kotlin.Unit> onBack, androidx.compose.ui.Modifier modifier) {
    }
}