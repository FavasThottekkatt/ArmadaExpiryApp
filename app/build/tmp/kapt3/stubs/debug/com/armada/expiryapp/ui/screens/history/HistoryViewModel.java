package com.armada.expiryapp.ui.screens.history;

import android.content.Context;
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
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B+\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\u000e\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0018J\u000e\u0010@\u001a\u00020>2\u0006\u0010A\u001a\u00020\u0012J\u0006\u0010B\u001a\u00020>J\b\u0010C\u001a\u00020>H\u0002J\u0006\u0010D\u001a\u00020>J\u0006\u0010E\u001a\u00020>J\u0006\u0010F\u001a\u00020>J\u0006\u0010G\u001a\u00020>R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u001a\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u001c0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u001c0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020 0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016R\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020 0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020 0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0016R\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020 0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020 0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0016R\u0014\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00180(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00180*\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020.0(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010/\u001a\b\u0012\u0004\u0012\u00020.0*\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010,R\u001d\u00101\u001a\b\u0012\u0004\u0012\u0002020\u0014\u00a2\u0006\u000e\n\u0000\u0012\u0004\b3\u00104\u001a\u0004\b5\u0010\u0016R#\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002090807\u00a2\u0006\u000e\n\u0000\u0012\u0004\b:\u00104\u001a\u0004\b;\u0010<\u00a8\u0006H"}, d2 = {"Lcom/armada/expiryapp/ui/screens/history/HistoryViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "entryRepository", "Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;", "outletRepository", "Lcom/armada/expiryapp/data/repository/OutletRepository;", "<init>", "(Landroid/content/Context;Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;Lcom/armada/expiryapp/data/repository/OutletRepository;)V", "getSessionHolder", "()Lcom/armada/expiryapp/data/session/SessionHolder;", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "_selectedOutlet", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "selectedOutlet", "Lkotlinx/coroutines/flow/StateFlow;", "getSelectedOutlet", "()Lkotlinx/coroutines/flow/StateFlow;", "_outletQuery", "", "outletQuery", "getOutletQuery", "_outletResults", "", "outletResults", "getOutletResults", "_showArchiveDialog", "", "showArchiveDialog", "getShowArchiveDialog", "_isArchiving", "isArchiving", "_isExporting", "isExporting", "_snackMessage", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "snackMessage", "Lkotlinx/coroutines/flow/SharedFlow;", "getSnackMessage", "()Lkotlinx/coroutines/flow/SharedFlow;", "_shareFile", "Ljava/io/File;", "shareFile", "getShareFile", "archivedCount", "", "getArchivedCount$annotations", "()V", "getArchivedCount", "archivedEntries", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "getArchivedEntries$annotations", "getArchivedEntries", "()Lkotlinx/coroutines/flow/Flow;", "setOutletQuery", "", "q", "selectOutlet", "outlet", "clearOutletSelection", "startOutletSearch", "requestArchive", "dismissArchiveDialog", "confirmArchive", "exportHistory", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HistoryViewModel extends androidx.lifecycle.ViewModel {
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
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _showArchiveDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showArchiveDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isArchiving = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isArchiving = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isExporting = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isExporting = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.io.File> _shareFile = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.io.File> shareFile = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> archivedCount = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> archivedEntries = null;
    
    @javax.inject.Inject()
    public HistoryViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
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
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowArchiveDialog() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isArchiving() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isExporting() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getSnackMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.io.File> getShareFile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getArchivedCount() {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @java.lang.Deprecated()
    public static void getArchivedCount$annotations() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> getArchivedEntries() {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @java.lang.Deprecated()
    public static void getArchivedEntries$annotations() {
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
    
    public final void requestArchive() {
    }
    
    public final void dismissArchiveDialog() {
    }
    
    public final void confirmArchive() {
    }
    
    public final void exportHistory() {
    }
}