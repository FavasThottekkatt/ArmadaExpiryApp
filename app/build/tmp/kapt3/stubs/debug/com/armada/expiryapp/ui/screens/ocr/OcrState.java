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

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b2\u0018\u00002\u00020\u0001:\u0004\u0004\u0005\u0006\u0007B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0004\b\t\n\u000b\u00a8\u0006\f"}, d2 = {"Lcom/armada/expiryapp/ui/screens/ocr/OcrState;", "", "<init>", "()V", "Scanning", "Found", "Multiple", "NotFound", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState$Found;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState$Multiple;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState$NotFound;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState$Scanning;", "app_debug"})
abstract class OcrState {
    
    private OcrState() {
        super();
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\bH\u00c6\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u00d6\u0003J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001e"}, d2 = {"Lcom/armada/expiryapp/ui/screens/ocr/OcrState$Found;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState;", "date", "Ljava/time/LocalDate;", "displayStr", "", "rawDigits", "confidence", "Lcom/armada/expiryapp/util/OcrDateParser$Confidence;", "<init>", "(Ljava/time/LocalDate;Ljava/lang/String;Ljava/lang/String;Lcom/armada/expiryapp/util/OcrDateParser$Confidence;)V", "getDate", "()Ljava/time/LocalDate;", "getDisplayStr", "()Ljava/lang/String;", "getRawDigits", "getConfidence", "()Lcom/armada/expiryapp/util/OcrDateParser$Confidence;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class Found extends com.armada.expiryapp.ui.screens.ocr.OcrState {
        @org.jetbrains.annotations.NotNull()
        private final java.time.LocalDate date = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String displayStr = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String rawDigits = null;
        @org.jetbrains.annotations.NotNull()
        private final com.armada.expiryapp.util.OcrDateParser.Confidence confidence = null;
        
        public Found(@org.jetbrains.annotations.NotNull()
        java.time.LocalDate date, @org.jetbrains.annotations.NotNull()
        java.lang.String displayStr, @org.jetbrains.annotations.NotNull()
        java.lang.String rawDigits, @org.jetbrains.annotations.NotNull()
        com.armada.expiryapp.util.OcrDateParser.Confidence confidence) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.time.LocalDate getDate() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDisplayStr() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getRawDigits() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.util.OcrDateParser.Confidence getConfidence() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.time.LocalDate component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.util.OcrDateParser.Confidence component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.ocr.OcrState.Found copy(@org.jetbrains.annotations.NotNull()
        java.time.LocalDate date, @org.jetbrains.annotations.NotNull()
        java.lang.String displayStr, @org.jetbrains.annotations.NotNull()
        java.lang.String rawDigits, @org.jetbrains.annotations.NotNull()
        com.armada.expiryapp.util.OcrDateParser.Confidence confidence) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0004\b\u0005\u0010\u0006J\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\n\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0013"}, d2 = {"Lcom/armada/expiryapp/ui/screens/ocr/OcrState$Multiple;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState;", "dates", "", "Ljava/time/LocalDate;", "<init>", "(Ljava/util/List;)V", "getDates", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Multiple extends com.armada.expiryapp.ui.screens.ocr.OcrState {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.time.LocalDate> dates = null;
        
        public Multiple(@org.jetbrains.annotations.NotNull()
        java.util.List<java.time.LocalDate> dates) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.time.LocalDate> getDates() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.time.LocalDate> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.ocr.OcrState.Multiple copy(@org.jetbrains.annotations.NotNull()
        java.util.List<java.time.LocalDate> dates) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/ocr/OcrState$NotFound;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState;", "<init>", "()V", "app_debug"})
    public static final class NotFound extends com.armada.expiryapp.ui.screens.ocr.OcrState {
        @org.jetbrains.annotations.NotNull()
        public static final com.armada.expiryapp.ui.screens.ocr.OcrState.NotFound INSTANCE = null;
        
        private NotFound() {
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/ocr/OcrState$Scanning;", "Lcom/armada/expiryapp/ui/screens/ocr/OcrState;", "<init>", "()V", "app_debug"})
    public static final class Scanning extends com.armada.expiryapp.ui.screens.ocr.OcrState {
        @org.jetbrains.annotations.NotNull()
        public static final com.armada.expiryapp.ui.screens.ocr.OcrState.Scanning INSTANCE = null;
        
        private Scanning() {
        }
    }
}