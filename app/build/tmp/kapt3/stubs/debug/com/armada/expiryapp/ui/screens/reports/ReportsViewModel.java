package com.armada.expiryapp.ui.screens.reports;

import android.content.Context;
import android.os.Environment;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Outlet;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import com.armada.expiryapp.util.AutoBackup;
import com.armada.expiryapp.util.ExcelExporter;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001:\u0001VB+\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\u000e\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u0018J\u000e\u0010D\u001a\u00020B2\u0006\u0010E\u001a\u00020\u0012J\u0006\u0010F\u001a\u00020BJ\b\u0010G\u001a\u00020BH\u0002J\u0016\u0010H\u001a\u00020B2\u0006\u0010E\u001a\u00020\u0012H\u0082@\u00a2\u0006\u0002\u0010IJ\u0006\u0010J\u001a\u00020BJ\u0006\u0010K\u001a\u00020BJ\u0006\u0010L\u001a\u00020BJ\u0006\u0010M\u001a\u00020BJ\u0006\u0010N\u001a\u00020BJ(\u0010O\u001a\u00020\u00182\u001e\u0010P\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0018\u0012\n\u0012\b\u0012\u0004\u0012\u0002060\u001c0Q0\u001cH\u0002J\b\u0010R\u001a\u00020BH\u0002J\u000e\u0010S\u001a\u00020B2\u0006\u0010T\u001a\u00020\u0018J\u0006\u0010U\u001a\u00020BR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u001a\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u001c0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u001c0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0016\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010 0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010 0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016R\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020$0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0016R\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020(0\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020(0*\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00180\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00180*\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010,R\u0014\u00100\u001a\b\u0012\u0004\u0012\u00020$0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u00101\u001a\b\u0012\u0004\u0012\u00020$0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0016R#\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060504\u00a2\u0006\u000e\n\u0000\u0012\u0004\b7\u00108\u001a\u0004\b9\u0010:R\u0014\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00180\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00180*\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010,R\u001a\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\u001c0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010?\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\u001c0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\u0016\u00a8\u0006W"}, d2 = {"Lcom/armada/expiryapp/ui/screens/reports/ReportsViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "entryRepository", "Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;", "outletRepository", "Lcom/armada/expiryapp/data/repository/OutletRepository;", "<init>", "(Landroid/content/Context;Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;Lcom/armada/expiryapp/data/repository/OutletRepository;)V", "getSessionHolder", "()Lcom/armada/expiryapp/data/session/SessionHolder;", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "_selectedOutlet", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "selectedOutlet", "Lkotlinx/coroutines/flow/StateFlow;", "getSelectedOutlet", "()Lkotlinx/coroutines/flow/StateFlow;", "_outletQuery", "", "outletQuery", "getOutletQuery", "_outletResults", "", "outletResults", "getOutletResults", "_summaryData", "Lcom/armada/expiryapp/ui/screens/reports/ReportsViewModel$SummaryData;", "summaryData", "getSummaryData", "_isExportingExcel", "", "isExportingExcel", "_shareFile", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Ljava/io/File;", "shareFile", "Lkotlinx/coroutines/flow/SharedFlow;", "getShareFile", "()Lkotlinx/coroutines/flow/SharedFlow;", "_shareText", "shareText", "getShareText", "_showTextScopeDialog", "showTextScopeDialog", "getShowTextScopeDialog", "reportEntries", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "getReportEntries$annotations", "()V", "getReportEntries", "()Lkotlinx/coroutines/flow/Flow;", "_snackMessage", "snackMessage", "getSnackMessage", "_pastExports", "pastExports", "getPastExports", "setOutletQuery", "", "q", "selectOutlet", "outlet", "clearOutletSelection", "startOutletSearch", "loadSummary", "(Lcom/armada/expiryapp/data/db/entity/Outlet;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportExcel", "requestTextReport", "dismissTextScopeDialog", "shareTextThisOutlet", "shareTextAllOutlets", "buildTextReport", "sections", "Lkotlin/Pair;", "triggerAutoBackup", "emitSnack", "msg", "loadPastExports", "SummaryData", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReportsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.session.SessionHolder sessionHolder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.OutletRepository outletRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.data.db.entity.Outlet> _selectedOutlet = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.data.db.entity.Outlet> selectedOutlet = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _outletQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> outletQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> _outletResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> outletResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.reports.ReportsViewModel.SummaryData> _summaryData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.reports.ReportsViewModel.SummaryData> summaryData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isExportingExcel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isExportingExcel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.io.File> _shareFile = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.io.File> shareFile = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _shareText = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> shareText = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _showTextScopeDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showTextScopeDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> reportEntries = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<java.io.File>> _pastExports = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.io.File>> pastExports = null;
    
    @javax.inject.Inject()
    public ReportsViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.session.SessionHolder sessionHolder, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.OutletRepository outletRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.session.SessionHolder getSessionHolder() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.data.db.entity.Outlet> getSelectedOutlet() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getOutletQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> getOutletResults() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.reports.ReportsViewModel.SummaryData> getSummaryData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isExportingExcel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.io.File> getShareFile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getShareText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowTextScopeDialog() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> getReportEntries() {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @java.lang.Deprecated()
    public static void getReportEntries$annotations() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getSnackMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.io.File>> getPastExports() {
        return null;
    }
    
    public final void setOutletQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String q) {
    }
    
    public final void selectOutlet(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.Outlet outlet) {
    }
    
    public final void clearOutletSelection() {
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.FlowPreview.class})
    private final void startOutletSearch() {
    }
    
    private final java.lang.Object loadSummary(com.armada.expiryapp.data.db.entity.Outlet outlet, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void exportExcel() {
    }
    
    public final void requestTextReport() {
    }
    
    public final void dismissTextScopeDialog() {
    }
    
    public final void shareTextThisOutlet() {
    }
    
    public final void shareTextAllOutlets() {
    }
    
    private final java.lang.String buildTextReport(java.util.List<? extends kotlin.Pair<java.lang.String, ? extends java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>>> sections) {
        return null;
    }
    
    private final void triggerAutoBackup() {
    }
    
    public final void emitSnack(@org.jetbrains.annotations.NotNull()
    java.lang.String msg) {
    }
    
    public final void loadPastExports() {
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u00a2\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003JO\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\"\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000e\u00a8\u0006#"}, d2 = {"Lcom/armada/expiryapp/ui/screens/reports/ReportsViewModel$SummaryData;", "", "outletName", "", "itemCount", "", "expiredCount", "within30Count", "merchandiser", "salesman", "monthLabel", "<init>", "(Ljava/lang/String;IIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getOutletName", "()Ljava/lang/String;", "getItemCount", "()I", "getExpiredCount", "getWithin30Count", "getMerchandiser", "getSalesman", "getMonthLabel", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    public static final class SummaryData {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String outletName = null;
        private final int itemCount = 0;
        private final int expiredCount = 0;
        private final int within30Count = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String merchandiser = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String salesman = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String monthLabel = null;
        
        public SummaryData(@org.jetbrains.annotations.NotNull()
        java.lang.String outletName, int itemCount, int expiredCount, int within30Count, @org.jetbrains.annotations.NotNull()
        java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
        java.lang.String salesman, @org.jetbrains.annotations.NotNull()
        java.lang.String monthLabel) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getOutletName() {
            return null;
        }
        
        public final int getItemCount() {
            return 0;
        }
        
        public final int getExpiredCount() {
            return 0;
        }
        
        public final int getWithin30Count() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMerchandiser() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSalesman() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMonthLabel() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final int component4() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component7() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.reports.ReportsViewModel.SummaryData copy(@org.jetbrains.annotations.NotNull()
        java.lang.String outletName, int itemCount, int expiredCount, int within30Count, @org.jetbrains.annotations.NotNull()
        java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
        java.lang.String salesman, @org.jetbrains.annotations.NotNull()
        java.lang.String monthLabel) {
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
}