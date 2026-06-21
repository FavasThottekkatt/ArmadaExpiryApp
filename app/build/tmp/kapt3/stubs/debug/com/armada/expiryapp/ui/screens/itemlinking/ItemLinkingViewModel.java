package com.armada.expiryapp.ui.screens.itemlinking;

import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.OutletItemLink;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\b\u0010\tJ\b\u00100\u001a\u000201H\u0002J\u000e\u00102\u001a\u0002012\u0006\u00103\u001a\u00020\rJ\u000e\u00104\u001a\u0002012\u0006\u00105\u001a\u00020+J\u0006\u00106\u001a\u000201J\u0006\u00107\u001a\u000201J\u0006\u00108\u001a\u000201J\u0006\u00109\u001a\u000201R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\r8F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\r0\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001b0\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019R\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\r0#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\r0%\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R#\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0*0)\u00a2\u0006\u000e\n\u0000\u0012\u0004\b,\u0010-\u001a\u0004\b.\u0010/\u00a8\u0006:"}, d2 = {"Lcom/armada/expiryapp/ui/screens/itemlinking/ItemLinkingViewModel;", "Landroidx/lifecycle/ViewModel;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "itemRepository", "Lcom/armada/expiryapp/data/repository/ItemRepository;", "linkRepository", "Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;", "<init>", "(Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/ItemRepository;Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;)V", "getSessionHolder", "()Lcom/armada/expiryapp/data/session/SessionHolder;", "outletCode", "", "getOutletCode", "()Ljava/lang/String;", "outletName", "getOutletName", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "_searchQuery", "Lkotlinx/coroutines/flow/MutableStateFlow;", "searchQuery", "Lkotlinx/coroutines/flow/StateFlow;", "getSearchQuery", "()Lkotlinx/coroutines/flow/StateFlow;", "_linkedBarcodes", "", "linkedBarcodes", "getLinkedBarcodes", "_showClearDialog", "", "showClearDialog", "getShowClearDialog", "_snackMessage", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "snackMessage", "Lkotlinx/coroutines/flow/SharedFlow;", "getSnackMessage", "()Lkotlinx/coroutines/flow/SharedFlow;", "items", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/armada/expiryapp/data/db/entity/Item;", "getItems$annotations", "()V", "getItems", "()Lkotlinx/coroutines/flow/Flow;", "loadLinkedBarcodes", "", "setSearchQuery", "query", "toggleLink", "item", "linkAllShown", "requestClearAll", "dismissClearAll", "confirmClearAll", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ItemLinkingViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.session.SessionHolder sessionHolder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ItemRepository itemRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.OutletItemLinkRepository linkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Set<java.lang.String>> _linkedBarcodes = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> linkedBarcodes = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _showClearDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showClearDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.Item>> items = null;
    
    @javax.inject.Inject()
    public ItemLinkingViewModel(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.session.SessionHolder sessionHolder, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ItemRepository itemRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.OutletItemLinkRepository linkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.session.SessionHolder getSessionHolder() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOutletCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOutletName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> getLinkedBarcodes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowClearDialog() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getSnackMessage() {
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
    
    private final void loadLinkedBarcodes() {
    }
    
    public final void setSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void toggleLink(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.Item item) {
    }
    
    public final void linkAllShown() {
    }
    
    public final void requestClearAll() {
    }
    
    public final void dismissClearAll() {
    }
    
    public final void confirmClearAll() {
    }
}