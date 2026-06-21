package com.armada.expiryapp.ui.screens.dashboard;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.IntrinsicSize;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.ExposedDropdownMenuDefaults;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.paging.LoadState;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Outlet;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import com.armada.expiryapp.util.Status;
import java.time.LocalDate;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000Z\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a0\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a9\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a\"\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001ah\u0010\u0019\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\t2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\t0\u001e2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\u001c2\u0006\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\t2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a>\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\u000b2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001aX\u0010\'\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\t2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\u001c2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u001e2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020)\u0012\u0004\u0012\u00020\u00010\u001c2\u0006\u0010 \u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u00a8\u0006+"}, d2 = {"DashboardScreen", "", "viewModel", "Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel;", "onNextClick", "Lkotlin/Function0;", "onViewArchiveClick", "StatCard", "label", "", "count", "", "color", "Landroidx/compose/ui/graphics/Color;", "hasOutlet", "", "modifier", "Landroidx/compose/ui/Modifier;", "StatCard-XO-JAsU", "(Ljava/lang/String;IJZLandroidx/compose/ui/Modifier;)V", "EntryRecordCard", "entry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "today", "Ljava/time/LocalDate;", "SearchableDropdown", "value", "onValueChange", "Lkotlin/Function1;", "allSuggestions", "", "onSuggestionClick", "isError", "errorLabel", "SessionRecoveryBanner", "outletName", "entryCount", "onContinue", "onStartFresh", "OutletSearchField", "results", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "onOutletSelected", "app_debug"})
public final class DashboardScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DashboardScreen(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNextClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onViewArchiveClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntryRecordCard(com.armada.expiryapp.data.db.entity.ExpiryEntry entry, java.time.LocalDate today, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void SearchableDropdown(java.lang.String label, java.lang.String value, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, java.util.List<java.lang.String> allSuggestions, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSuggestionClick, boolean isError, java.lang.String errorLabel, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SessionRecoveryBanner(java.lang.String outletName, int entryCount, kotlin.jvm.functions.Function0<kotlin.Unit> onContinue, kotlin.jvm.functions.Function0<kotlin.Unit> onStartFresh, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void OutletSearchField(java.lang.String value, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, java.util.List<com.armada.expiryapp.data.db.entity.Outlet> results, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.data.db.entity.Outlet, kotlin.Unit> onOutletSelected, boolean isError, androidx.compose.ui.Modifier modifier) {
    }
}