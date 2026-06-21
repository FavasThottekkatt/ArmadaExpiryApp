package com.armada.expiryapp.ui.screens.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.BlendMode;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.core.content.ContextCompat;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import com.armada.expiryapp.util.ScanFeedback;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0007\u001a:\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0003\u001a4\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u0003H\u0002\u001a\u0012\u0010\u0014\u001a\u00020\u00012\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0003\u001a\u0016\u0010\u0017\u001a\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0003\u00a8\u0006\u0018"}, d2 = {"BarcodeScannerScreen", "", "onBarcodeDetected", "Lkotlin/Function1;", "", "onBack", "Lkotlin/Function0;", "CameraPreview", "lifecycleOwner", "Landroidx/lifecycle/LifecycleOwner;", "view", "Landroid/view/View;", "processFrame", "imageProxy", "Landroidx/camera/core/ImageProxy;", "barcodeScanner", "Lcom/google/mlkit/vision/barcode/BarcodeScanner;", "scanned", "Ljava/util/concurrent/atomic/AtomicBoolean;", "onDetected", "ScanOverlay", "modifier", "Landroidx/compose/ui/Modifier;", "PermissionDeniedContent", "app_debug"})
public final class BarcodeScannerScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void BarcodeScannerScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onBarcodeDetected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CameraPreview(androidx.lifecycle.LifecycleOwner lifecycleOwner, android.view.View view, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onBarcodeDetected, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    private static final void processFrame(androidx.camera.core.ImageProxy imageProxy, com.google.mlkit.vision.barcode.BarcodeScanner barcodeScanner, java.util.concurrent.atomic.AtomicBoolean scanned, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDetected) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ScanOverlay(androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PermissionDeniedContent(kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
}