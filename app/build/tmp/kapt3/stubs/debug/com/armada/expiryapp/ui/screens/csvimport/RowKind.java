package com.armada.expiryapp.ui.screens.csvimport;

import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.window.DialogProperties;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import com.armada.expiryapp.util.CsvParseResult;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/RowKind;", "", "<init>", "(Ljava/lang/String;I)V", "Ok", "Warn", "Error", "app_debug"})
enum RowKind {
    /*public static final*/ Ok /* = new Ok() */,
    /*public static final*/ Warn /* = new Warn() */,
    /*public static final*/ Error /* = new Error() */;
    
    RowKind() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.armada.expiryapp.ui.screens.csvimport.RowKind> getEntries() {
        return null;
    }
}