package com.armada.expiryapp.ui.screens.reports;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.core.content.FileProvider;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\\\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\r2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0010H\u0003\u001a\u0010\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0013H\u0003\u001a\'\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0018H\u0003\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a\b\u0010\u001b\u001a\u00020\u0001H\u0003\u001a\u0018\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0003\u001a,\u0010!\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\rH\u0003\u00a8\u0006\'"}, d2 = {"ReportsScreen", "", "viewModel", "Lcom/armada/expiryapp/ui/screens/reports/ReportsViewModel;", "OutletSelector", "query", "", "results", "", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "hasSelection", "", "onQueryChange", "Lkotlin/Function1;", "onSelect", "onClear", "Lkotlin/Function0;", "SummaryCard", "s", "Lcom/armada/expiryapp/ui/screens/reports/ReportsViewModel$SummaryData;", "SummaryStatItem", "label", "value", "color", "Landroidx/compose/ui/graphics/Color;", "SummaryStatItem-mxwnekA", "(Ljava/lang/String;Ljava/lang/String;J)V", "ReportTableHeader", "ReportTableRow", "entry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "index", "", "PastExportRow", "file", "Ljava/io/File;", "context", "Landroid/content/Context;", "onShareError", "app_debug"})
@kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
public final class ReportsScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void ReportsScreen(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.reports.ReportsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OutletSelector(java.lang.String query, java.util.List<com.armada.expiryapp.data.db.entity.Outlet> results, boolean hasSelection, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.data.db.entity.Outlet, kotlin.Unit> onSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onClear) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SummaryCard(com.armada.expiryapp.ui.screens.reports.ReportsViewModel.SummaryData s) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReportTableHeader() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReportTableRow(com.armada.expiryapp.data.db.entity.ExpiryEntry entry, int index) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PastExportRow(java.io.File file, android.content.Context context, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onShareError) {
    }
}