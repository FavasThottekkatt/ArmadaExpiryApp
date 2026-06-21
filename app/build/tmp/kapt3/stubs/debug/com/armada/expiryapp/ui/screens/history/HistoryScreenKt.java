package com.armada.expiryapp.ui.screens.history;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.core.content.FileProvider;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Outlet;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a^\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\b\u0010\n\u001a\u0004\u0018\u00010\t2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0003\u001a\u0018\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0003\u00a8\u0006\u0015"}, d2 = {"HistoryScreen", "", "viewModel", "Lcom/armada/expiryapp/ui/screens/history/HistoryViewModel;", "HistoryOutletSelector", "query", "", "results", "", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "selectedOutlet", "onQueryChange", "Lkotlin/Function1;", "onSelect", "onClear", "Lkotlin/Function0;", "HistoryEntryRow", "entry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "isEven", "", "app_debug"})
public final class HistoryScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void HistoryScreen(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.history.HistoryViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HistoryOutletSelector(java.lang.String query, java.util.List<com.armada.expiryapp.data.db.entity.Outlet> results, com.armada.expiryapp.data.db.entity.Outlet selectedOutlet, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.data.db.entity.Outlet, kotlin.Unit> onSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onClear) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HistoryEntryRow(com.armada.expiryapp.data.db.entity.ExpiryEntry entry, boolean isEven) {
    }
}