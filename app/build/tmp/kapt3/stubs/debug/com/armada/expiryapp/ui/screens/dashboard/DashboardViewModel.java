package com.armada.expiryapp.ui.screens.dashboard;

import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Outlet;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.DeviceLockRepository;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
import com.armada.expiryapp.data.repository.TeamLinkRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import java.time.LocalDate;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0011\b\u0007\u0018\u00002\u00020\u0001:\u0003RSTBA\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0004\b\u0010\u0010\u0011J\b\u0010C\u001a\u00020DH\u0002J\b\u0010E\u001a\u00020DH\u0002J\b\u0010F\u001a\u00020DH\u0002J\b\u0010G\u001a\u00020DH\u0002J\b\u0010H\u001a\u00020DH\u0002J\u0006\u0010I\u001a\u00020DJ\u000e\u0010J\u001a\u00020D2\u0006\u0010K\u001a\u00020/J\u000e\u0010L\u001a\u00020D2\u0006\u0010M\u001a\u00020\u001dJ\u000e\u0010N\u001a\u00020D2\u0006\u0010O\u001a\u00020\'J\u0006\u0010P\u001a\u00020\u0016J\u000e\u0010Q\u001a\u00020D2\u0006\u0010M\u001a\u00020\u001dR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0017R\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00190\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001d0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017R\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0017R\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u00190\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u00190\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0017R\u0014\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00160\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0017R\u0017\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0017R\u0016\u0010.\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010/0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u00100\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010/0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u0017R\u0016\u00102\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001030\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u00104\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001030\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u0017R\u0017\u00106\u001a\b\u0012\u0004\u0012\u0002070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u0017R\u0014\u00109\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010;\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\u0017R\u001d\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020@0?0>\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010B\u00a8\u0006U"}, d2 = {"Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "outletRepository", "Lcom/armada/expiryapp/data/repository/OutletRepository;", "entryRepository", "Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;", "itemRepository", "Lcom/armada/expiryapp/data/repository/ItemRepository;", "csvMetadataRepository", "Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "deviceLockRepository", "Lcom/armada/expiryapp/data/repository/DeviceLockRepository;", "teamLinkRepository", "Lcom/armada/expiryapp/data/repository/TeamLinkRepository;", "<init>", "(Lcom/armada/expiryapp/data/repository/OutletRepository;Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;Lcom/armada/expiryapp/data/repository/ItemRepository;Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/DeviceLockRepository;Lcom/armada/expiryapp/data/repository/TeamLinkRepository;)V", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "isTeamLinkingComplete", "Lkotlinx/coroutines/flow/StateFlow;", "", "()Lkotlinx/coroutines/flow/StateFlow;", "teamLinks", "", "Lcom/armada/expiryapp/data/db/entity/TeamLink;", "_currentMerchandiser", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_currentSalesman", "_outletName", "outletName", "getOutletName", "_outletCode", "outletCode", "getOutletCode", "_outletQuery", "_outletResults", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "outletResults", "getOutletResults", "_showFieldErrors", "showFieldErrors", "getShowFieldErrors", "isFormComplete", "_sessionRecovery", "Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel$SessionRecovery;", "sessionRecovery", "getSessionRecovery", "_masterDataInfo", "Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel$MasterDataInfo;", "masterDataInfo", "getMasterDataInfo", "stats", "Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel$DashboardStats;", "getStats", "_rawSearchQuery", "_debouncedSearchQuery", "searchQuery", "getSearchQuery", "latestEntries", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "getLatestEntries", "()Lkotlinx/coroutines/flow/Flow;", "startOutletSearch", "", "startSearchDebounce", "checkSessionRecovery", "loadLockedMerchandiser", "loadMasterDataInfo", "dismissSessionRecovery", "continueSession", "recovery", "setOutletQuery", "query", "selectOutlet", "outlet", "onNextTapped", "setSearchQuery", "DashboardStats", "SessionRecovery", "MasterDataInfo", "app_debug"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class, kotlinx.coroutines.FlowPreview.class})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.OutletRepository outletRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ItemRepository itemRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.session.SessionHolder sessionHolder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.DeviceLockRepository deviceLockRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.TeamLinkRepository teamLinkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isTeamLinkingComplete = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.TeamLink>> teamLinks = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _currentMerchandiser = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _currentSalesman = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _outletName = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> outletName = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _outletCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> outletCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _outletQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> _outletResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> outletResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _showFieldErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showFieldErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFormComplete = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.SessionRecovery> _sessionRecovery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.SessionRecovery> sessionRecovery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.MasterDataInfo> _masterDataInfo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.MasterDataInfo> masterDataInfo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.DashboardStats> stats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _rawSearchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _debouncedSearchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> latestEntries = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.OutletRepository outletRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ItemRepository itemRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.session.SessionHolder sessionHolder, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.DeviceLockRepository deviceLockRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.TeamLinkRepository teamLinkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isTeamLinkingComplete() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getOutletName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getOutletCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> getOutletResults() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowFieldErrors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFormComplete() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.SessionRecovery> getSessionRecovery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.MasterDataInfo> getMasterDataInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.DashboardStats> getStats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> getLatestEntries() {
        return null;
    }
    
    private final void startOutletSearch() {
    }
    
    private final void startSearchDebounce() {
    }
    
    private final void checkSessionRecovery() {
    }
    
    private final void loadLockedMerchandiser() {
    }
    
    private final void loadMasterDataInfo() {
    }
    
    public final void dismissSessionRecovery() {
    }
    
    public final void continueSession(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.SessionRecovery recovery) {
    }
    
    public final void setOutletQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void selectOutlet(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.Outlet outlet) {
    }
    
    public final boolean onNextTapped() {
        return false;
    }
    
    public final void setSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n\u00a8\u0006\u0019"}, d2 = {"Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel$DashboardStats;", "", "expired", "", "within30", "within60", "within90", "<init>", "(IIII)V", "getExpired", "()I", "getWithin30", "getWithin60", "getWithin90", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class DashboardStats {
        private final int expired = 0;
        private final int within30 = 0;
        private final int within60 = 0;
        private final int within90 = 0;
        
        public DashboardStats(int expired, int within30, int within60, int within90) {
            super();
        }
        
        public final int getExpired() {
            return 0;
        }
        
        public final int getWithin30() {
            return 0;
        }
        
        public final int getWithin60() {
            return 0;
        }
        
        public final int getWithin90() {
            return 0;
        }
        
        public DashboardStats() {
            super();
        }
        
        public final int component1() {
            return 0;
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
        public final com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.DashboardStats copy(int expired, int within30, int within60, int within90) {
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
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0006H\u00c6\u0003J\'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel$MasterDataInfo;", "", "itemCount", "", "outletCount", "importedAt", "", "<init>", "(IILjava/lang/String;)V", "getItemCount", "()I", "getOutletCount", "getImportedAt", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    public static final class MasterDataInfo {
        private final int itemCount = 0;
        private final int outletCount = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String importedAt = null;
        
        public MasterDataInfo(int itemCount, int outletCount, @org.jetbrains.annotations.NotNull()
        java.lang.String importedAt) {
            super();
        }
        
        public final int getItemCount() {
            return 0;
        }
        
        public final int getOutletCount() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getImportedAt() {
            return null;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.MasterDataInfo copy(int itemCount, int outletCount, @org.jetbrains.annotations.NotNull()
        java.lang.String importedAt) {
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
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0006H\u00c6\u0003J\'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0006H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/armada/expiryapp/ui/screens/dashboard/DashboardViewModel$SessionRecovery;", "", "outletName", "", "outletCode", "entryCount", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V", "getOutletName", "()Ljava/lang/String;", "getOutletCode", "getEntryCount", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    public static final class SessionRecovery {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String outletName = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String outletCode = null;
        private final int entryCount = 0;
        
        public SessionRecovery(@org.jetbrains.annotations.NotNull()
        java.lang.String outletName, @org.jetbrains.annotations.NotNull()
        java.lang.String outletCode, int entryCount) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getOutletName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getOutletCode() {
            return null;
        }
        
        public final int getEntryCount() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        public final int component3() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel.SessionRecovery copy(@org.jetbrains.annotations.NotNull()
        java.lang.String outletName, @org.jetbrains.annotations.NotNull()
        java.lang.String outletCode, int entryCount) {
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