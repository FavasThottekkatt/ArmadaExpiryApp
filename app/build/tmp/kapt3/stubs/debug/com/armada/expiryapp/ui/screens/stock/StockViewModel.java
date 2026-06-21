package com.armada.expiryapp.ui.screens.stock;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.StockEntry;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.StockEntryRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.StateFlow;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001:\u0001RB+\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\b\u0010:\u001a\u00020;H\u0002J\u000e\u0010<\u001a\u00020;2\u0006\u0010=\u001a\u00020\u0017J\u0006\u0010>\u001a\u00020;J \u0010?\u001a\u00020;2\u0006\u0010@\u001a\u00020\u00172\u0006\u0010A\u001a\u00020\u00172\b\u0010B\u001a\u0004\u0018\u00010\u0017J \u0010C\u001a\u00020;2\u0006\u0010@\u001a\u00020\u00172\u0006\u0010A\u001a\u00020\u00172\b\u0010B\u001a\u0004\u0018\u00010\u0017J\u0006\u0010D\u001a\u00020;J\u0006\u0010E\u001a\u00020;J\u000e\u0010F\u001a\u00020;2\u0006\u0010G\u001a\u00020HJ\u000e\u0010I\u001a\u00020;2\u0006\u0010J\u001a\u00020\u0017J\u0006\u0010K\u001a\u00020;J\u0006\u0010L\u001a\u00020;J\u0006\u0010M\u001a\u00020;J\u0006\u0010N\u001a\u00020;J\u001e\u0010O\u001a\u00020;2\u0006\u0010@\u001a\u00020\u00172\u0006\u0010P\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010QR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0010\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R \u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00180\u00160\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\u0019\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00180\u00160\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00170\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00170\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001cR\u0016\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001cR\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00170\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00170\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001cR\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00110\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00110\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0014\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00170*X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00170,\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0014\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00170*X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u00100\u001a\b\u0012\u0004\u0012\u00020\u00170,\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010.R#\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002050403\u00a2\u0006\u000e\n\u0000\u0012\u0004\b6\u00107\u001a\u0004\b8\u00109\u00a8\u0006S"}, d2 = {"Lcom/armada/expiryapp/ui/screens/stock/StockViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "stockRepository", "Lcom/armada/expiryapp/data/repository/StockEntryRepository;", "itemRepository", "Lcom/armada/expiryapp/data/repository/ItemRepository;", "<init>", "(Landroid/content/Context;Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/StockEntryRepository;Lcom/armada/expiryapp/data/repository/ItemRepository;)V", "getSessionHolder", "()Lcom/armada/expiryapp/data/session/SessionHolder;", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "hasOutlet", "", "getHasOutlet", "()Z", "_stockMap", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "Lcom/armada/expiryapp/ui/screens/stock/StockViewModel$StockState;", "stockMap", "Lkotlinx/coroutines/flow/StateFlow;", "getStockMap", "()Lkotlinx/coroutines/flow/StateFlow;", "_searchQuery", "searchQuery", "getSearchQuery", "_focusedBarcode", "focusedBarcode", "getFocusedBarcode", "_selectedUnit", "selectedUnit", "getSelectedUnit", "_showClearAllDialog", "showClearAllDialog", "getShowClearAllDialog", "_snackMessage", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "snackMessage", "Lkotlinx/coroutines/flow/SharedFlow;", "getSnackMessage", "()Lkotlinx/coroutines/flow/SharedFlow;", "_shareText", "shareText", "getShareText", "items", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/armada/expiryapp/data/db/entity/Item;", "getItems$annotations", "()V", "getItems", "()Lkotlinx/coroutines/flow/Flow;", "loadStockState", "", "setSearchQuery", "q", "clearSearch", "toggleOos", "barcode", "description", "productCode", "tapQtyField", "stockClearSingle", "stockClearAll", "appendDigit", "digit", "", "setUnit", "unit", "shareReport", "requestClearAll", "dismissClearAll", "confirmClearAll", "upsertState", "state", "(Ljava/lang/String;Lcom/armada/expiryapp/ui/screens/stock/StockViewModel$StockState;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "StockState", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StockViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.session.SessionHolder sessionHolder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.StockEntryRepository stockRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ItemRepository itemRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, com.armada.expiryapp.ui.screens.stock.StockViewModel.StockState>> _stockMap = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, com.armada.expiryapp.ui.screens.stock.StockViewModel.StockState>> stockMap = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _focusedBarcode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> focusedBarcode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _selectedUnit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> selectedUnit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _showClearAllDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showClearAllDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _shareText = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> shareText = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.Item>> items = null;
    
    @javax.inject.Inject()
    public StockViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.session.SessionHolder sessionHolder, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.StockEntryRepository stockRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ItemRepository itemRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.session.SessionHolder getSessionHolder() {
        return null;
    }
    
    public final boolean getHasOutlet() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, com.armada.expiryapp.ui.screens.stock.StockViewModel.StockState>> getStockMap() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getFocusedBarcode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSelectedUnit() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowClearAllDialog() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getSnackMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getShareText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.Item>> getItems() {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class, kotlinx.coroutines.FlowPreview.class})
    @java.lang.Deprecated()
    public static void getItems$annotations() {
    }
    
    private final void loadStockState() {
    }
    
    public final void setSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String q) {
    }
    
    public final void clearSearch() {
    }
    
    public final void toggleOos(@org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.String productCode) {
    }
    
    public final void tapQtyField(@org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.String productCode) {
    }
    
    public final void stockClearSingle() {
    }
    
    public final void stockClearAll() {
    }
    
    public final void appendDigit(int digit) {
    }
    
    public final void setUnit(@org.jetbrains.annotations.NotNull()
    java.lang.String unit) {
    }
    
    public final void shareReport() {
    }
    
    public final void requestClearAll() {
    }
    
    public final void dismissClearAll() {
    }
    
    public final void confirmClearAll() {
    }
    
    private final java.lang.Object upsertState(java.lang.String barcode, com.armada.expiryapp.ui.screens.stock.StockViewModel.StockState state, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0019\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0004\b\f\u0010\rJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\tH\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\tH\u00c6\u0003JG\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\tH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u00052\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020\u0007H\u00d6\u0001J\t\u0010!\u001a\u00020\tH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014\u00a8\u0006\""}, d2 = {"Lcom/armada/expiryapp/ui/screens/stock/StockViewModel$StockState;", "", "stockEntryId", "", "isOos", "", "quantity", "", "unit", "", "description", "productCode", "<init>", "(JZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getStockEntryId", "()J", "()Z", "getQuantity", "()I", "getUnit", "()Ljava/lang/String;", "getDescription", "getProductCode", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
    public static final class StockState {
        private final long stockEntryId = 0L;
        private final boolean isOos = false;
        private final int quantity = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String unit = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String description = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String productCode = null;
        
        public StockState(long stockEntryId, boolean isOos, int quantity, @org.jetbrains.annotations.NotNull()
        java.lang.String unit, @org.jetbrains.annotations.NotNull()
        java.lang.String description, @org.jetbrains.annotations.Nullable()
        java.lang.String productCode) {
            super();
        }
        
        public final long getStockEntryId() {
            return 0L;
        }
        
        public final boolean isOos() {
            return false;
        }
        
        public final int getQuantity() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getUnit() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDescription() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getProductCode() {
            return null;
        }
        
        public StockState() {
            super();
        }
        
        public final long component1() {
            return 0L;
        }
        
        public final boolean component2() {
            return false;
        }
        
        public final int component3() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.stock.StockViewModel.StockState copy(long stockEntryId, boolean isOos, int quantity, @org.jetbrains.annotations.NotNull()
        java.lang.String unit, @org.jetbrains.annotations.NotNull()
        java.lang.String description, @org.jetbrains.annotations.Nullable()
        java.lang.String productCode) {
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