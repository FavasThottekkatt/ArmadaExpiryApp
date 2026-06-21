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

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001a4\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u0010\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0010H\u0003\u001a(\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0003\u00a8\u0006\u0018"}, d2 = {"CsvValidationScreen", "", "uiState", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "onConfirmImport", "Lkotlin/Function1;", "Lcom/armada/expiryapp/util/CsvParseResult;", "onCancel", "Lkotlin/Function0;", "ValidationCard", "result", "isFirstImport", "", "onConfirm", "SectionLabel", "title", "", "ValidationRow", "icon", "label", "value", "", "kind", "Lcom/armada/expiryapp/ui/screens/csvimport/RowKind;", "app_debug"})
public final class CsvValidationScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void CsvValidationScreen(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState uiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.armada.expiryapp.util.CsvParseResult, kotlin.Unit> onConfirmImport, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ValidationCard(com.armada.expiryapp.util.CsvParseResult result, boolean isFirstImport, kotlin.jvm.functions.Function0<kotlin.Unit> onConfirm, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionLabel(java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ValidationRow(java.lang.String icon, java.lang.String label, int value, com.armada.expiryapp.ui.screens.csvimport.RowKind kind) {
    }
}