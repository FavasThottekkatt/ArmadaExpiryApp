package com.armada.expiryapp.ui.screens.entry;

import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.FlowPreview;
import com.armada.expiryapp.data.db.entity.Item;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a8\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u001d\n\u0002\u0010\t\n\u0002\b\"\b\u0007\u0018\u0000 \u00a0\u00012\u00020\u0001:\n\u009c\u0001\u009d\u0001\u009e\u0001\u009f\u0001\u00a0\u0001B)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\u000e\u0010^\u001a\u00020_2\u0006\u0010`\u001a\u00020aJ\u0006\u0010b\u001a\u00020_J\u0006\u0010c\u001a\u00020_J\u000e\u0010d\u001a\u00020_2\u0006\u0010e\u001a\u00020AJ\u000e\u0010f\u001a\u00020_2\u0006\u0010>\u001a\u00020\u0012J\u000e\u0010g\u001a\u00020_2\u0006\u0010h\u001a\u00020\u0012J\u000e\u0010i\u001a\u00020_2\u0006\u0010h\u001a\u00020\u0012J\u0006\u0010j\u001a\u00020_J\u0006\u0010k\u001a\u00020_J\u000e\u0010l\u001a\u00020_2\u0006\u0010m\u001a\u00020\u0012J\u0016\u0010n\u001a\u00020_2\u0006\u0010o\u001a\u00020\u0012H\u0082@\u00a2\u0006\u0002\u0010pJ\u0010\u0010q\u001a\u00020_2\u0006\u0010r\u001a\u00020/H\u0002J\u0006\u0010s\u001a\u00020_J\u0006\u0010t\u001a\u00020_J\u0006\u0010u\u001a\u00020_J\u0006\u0010v\u001a\u00020_J\b\u0010w\u001a\u00020MH\u0002J\u0010\u0010x\u001a\u00020_2\u0006\u0010y\u001a\u00020MH\u0002J\u0010\u0010z\u001a\u00020_2\u0006\u0010y\u001a\u00020MH\u0002J\u0010\u0010{\u001a\u00020_2\u0006\u0010y\u001a\u00020MH\u0002J\u0010\u0010|\u001a\u00020_2\u0006\u0010y\u001a\u00020MH\u0002J\u0017\u0010}\u001a\u00020_2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020aJ\u0007\u0010\u0081\u0001\u001a\u00020_J\u000f\u0010\u0082\u0001\u001a\u00020_2\u0006\u0010y\u001a\u00020MJ\u0007\u0010\u0083\u0001\u001a\u00020_J\u000f\u0010\u0084\u0001\u001a\u00020_2\u0006\u0010y\u001a\u00020MJ\u0007\u0010\u0085\u0001\u001a\u00020_J\u0010\u0010\u0086\u0001\u001a\u00020_2\u0007\u0010\u0087\u0001\u001a\u00020\u007fJ\u0007\u0010\u0088\u0001\u001a\u00020_J\u0007\u0010\u0089\u0001\u001a\u00020_J\u0010\u0010\u008a\u0001\u001a\u00020_2\u0007\u0010\u008b\u0001\u001a\u00020MJ\u0007\u0010\u008c\u0001\u001a\u00020_J\"\u0010\u008d\u0001\u001a\u00020_2\u0007\u0010\u008e\u0001\u001a\u00020\u00122\u0007\u0010\u008f\u0001\u001a\u00020a2\u0007\u0010\u0090\u0001\u001a\u00020\u0012J\t\u0010\u0091\u0001\u001a\u00020_H\u0002J\t\u0010\u0092\u0001\u001a\u00020_H\u0002J\u0007\u0010\u0093\u0001\u001a\u00020_J\u0007\u0010\u0094\u0001\u001a\u00020_J\u0010\u0010\u0095\u0001\u001a\u00020_2\u0007\u0010\u0096\u0001\u001a\u00020\u0012J\u0010\u0010\u0097\u0001\u001a\u00020_2\u0007\u0010\u0096\u0001\u001a\u00020\u0012J\u000f\u0010\u0098\u0001\u001a\u00020_2\u0006\u0010r\u001a\u00020/J\t\u0010\u0099\u0001\u001a\u00020_H\u0002J\u0012\u0010\u009a\u0001\u001a\u00020\u00182\u0007\u0010\u009b\u0001\u001a\u00020\u0012H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00180\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001eR\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00180\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001eR\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001eR\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001eR\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001eR\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001eR\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00180\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001eR\u001a\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0.0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u00100\u001a\b\u0012\u0004\u0012\u00020\u00180\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0.0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001eR\u0017\u00103\u001a\b\u0012\u0004\u0012\u00020\u00180\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001eR\u0014\u00104\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u00105\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010\u001eR\u0017\u00107\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u001eR\u0017\u00109\u001a\b\u0012\u0004\u0012\u00020\u00180\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010\u001eR\u0014\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u001eR\u0017\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00120\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010\u001eR\u0014\u0010@\u001a\b\u0012\u0004\u0012\u00020A0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010B\u001a\b\u0012\u0004\u0012\u00020A0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010\u001eR\u0014\u0010D\u001a\b\u0012\u0004\u0012\u00020E0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010F\u001a\b\u0012\u0004\u0012\u00020E0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010\u001eR\u0014\u0010H\u001a\b\u0012\u0004\u0012\u00020I0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010J\u001a\b\u0012\u0004\u0012\u00020I0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010\u001eR\u0016\u0010L\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010M0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010N\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010M0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010\u001eR\u0010\u0010P\u001a\u0004\u0018\u00010MX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010Q\u001a\b\u0012\u0004\u0012\u00020\u00120RX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010S\u001a\b\u0012\u0004\u0012\u00020\u00120T\u00a2\u0006\b\n\u0000\u001a\u0004\bU\u0010VR\'\u0010W\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020M0Y0X8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\\\u0010]\u001a\u0004\bZ\u0010[\u00a8\u0006\u00a1\u0001"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel;", "Landroidx/lifecycle/ViewModel;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "entryRepository", "Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;", "itemRepository", "Lcom/armada/expiryapp/data/repository/ItemRepository;", "linkRepository", "Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;", "<init>", "(Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;Lcom/armada/expiryapp/data/repository/ItemRepository;Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;)V", "getSessionHolder", "()Lcom/armada/expiryapp/data/session/SessionHolder;", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "_barcode", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_description", "_productCode", "_descriptionQuery", "_productCodeQuery", "_itemFilled", "", "_isBulkMode", "_lastSavedItem", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$BulkItem;", "isBulkMode", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "showRepeatButton", "getShowRepeatButton", "barcode", "getBarcode", "description", "getDescription", "productCode", "getProductCode", "descriptionQuery", "getDescriptionQuery", "productCodeQuery", "getProductCodeQuery", "itemFilled", "getItemFilled", "_descriptionResults", "", "Lcom/armada/expiryapp/data/db/entity/Item;", "_isSearching", "descriptionResults", "getDescriptionResults", "isSearching", "_expiryDateRaw", "expiryDateRaw", "getExpiryDateRaw", "expiryDateDisplay", "getExpiryDateDisplay", "isDateComplete", "_quantity", "_unit", "quantity", "getQuantity", "unit", "getUnit", "_activeField", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$ActiveField;", "activeField", "getActiveField", "_saveDialogState", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState;", "saveDialogState", "getSaveDialogState", "_unlinkedItemState", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState;", "unlinkedItemState", "getUnlinkedItemState", "_editingEntry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "editingEntry", "getEditingEntry", "lastDeletedEntry", "_snackMessage", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "snackMessage", "Lkotlinx/coroutines/flow/SharedFlow;", "getSnackMessage", "()Lkotlinx/coroutines/flow/SharedFlow;", "latestEntries", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "getLatestEntries", "()Lkotlinx/coroutines/flow/Flow;", "latestEntries$delegate", "Lkotlin/Lazy;", "onNumpadDigit", "", "digit", "", "onNumpadClearSingle", "onNumpadClearAll", "setActiveField", "field", "setUnit", "setDescriptionQuery", "query", "setProductCodeQuery", "clearItem", "lookupBarcode", "onBarcodeScanned", "rawBarcode", "fillFromBarcode", "bc", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "applyItem", "item", "onUnlinkedItemConfirm", "onUnlinkedItemDismiss", "lookupProductCode", "saveEntry", "buildPendingEntry", "runValidationPipeline", "pending", "checkPastDate", "checkQty", "commitSave", "onDuplicateConfirmMerge", "existingId", "", "additionalQty", "onDuplicateDismiss", "onPastDateConfirmed", "onPastDateDismiss", "onQtyConfirmed", "onQtyDismiss", "deleteEntry", "id", "undoDelete", "clearPendingDelete", "startEditEntry", "entry", "cancelEdit", "saveEditedEntry", "newExpiryIso", "newQty", "newUnit", "clearEntry", "clearEntryForBulk", "activateBulkMode", "deactivateBulkMode", "onOcrDateScanned", "rawDigits", "setExpiryDateFromCalendar", "onDescriptionItemSelected", "startDescriptionSearch", "isDateValid", "raw", "ActiveField", "SaveDialogState", "UnlinkedItemState", "BulkItem", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NewEntryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.session.SessionHolder sessionHolder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ItemRepository itemRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.OutletItemLinkRepository linkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _barcode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _description = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _productCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _descriptionQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _productCodeQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _itemFilled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isBulkMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.BulkItem> _lastSavedItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isBulkMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showRepeatButton = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> barcode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> description = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> productCode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> descriptionQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> productCodeQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> itemFilled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Item>> _descriptionResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isSearching = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Item>> descriptionResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSearching = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _expiryDateRaw = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> expiryDateRaw = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> expiryDateDisplay = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isDateComplete = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _quantity = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _unit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> quantity = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> unit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField> _activeField = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField> activeField = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState> _saveDialogState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState> saveDialogState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState> _unlinkedItemState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState> unlinkedItemState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.data.db.entity.ExpiryEntry> _editingEntry = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.data.db.entity.ExpiryEntry> editingEntry = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.armada.expiryapp.data.db.entity.ExpiryEntry lastDeletedEntry;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy latestEntries$delegate = null;
    private static final java.time.format.DateTimeFormatter TIMESTAMP_FMT = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public NewEntryViewModel(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.session.SessionHolder sessionHolder, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ItemRepository itemRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.OutletItemLinkRepository linkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.session.SessionHolder getSessionHolder() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isBulkMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowRepeatButton() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getBarcode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getProductCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getDescriptionQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getProductCodeQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getItemFilled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.Item>> getDescriptionResults() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSearching() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getExpiryDateRaw() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getExpiryDateDisplay() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isDateComplete() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getQuantity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getUnit() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField> getActiveField() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState> getSaveDialogState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState> getUnlinkedItemState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.data.db.entity.ExpiryEntry> getEditingEntry() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getSnackMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.armada.expiryapp.data.db.entity.ExpiryEntry>> getLatestEntries() {
        return null;
    }
    
    public final void onNumpadDigit(int digit) {
    }
    
    public final void onNumpadClearSingle() {
    }
    
    public final void onNumpadClearAll() {
    }
    
    public final void setActiveField(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField field) {
    }
    
    public final void setUnit(@org.jetbrains.annotations.NotNull()
    java.lang.String unit) {
    }
    
    public final void setDescriptionQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void setProductCodeQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void clearItem() {
    }
    
    public final void lookupBarcode() {
    }
    
    public final void onBarcodeScanned(@org.jetbrains.annotations.NotNull()
    java.lang.String rawBarcode) {
    }
    
    private final java.lang.Object fillFromBarcode(java.lang.String bc, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void applyItem(com.armada.expiryapp.data.db.entity.Item item) {
    }
    
    public final void onUnlinkedItemConfirm() {
    }
    
    public final void onUnlinkedItemDismiss() {
    }
    
    public final void lookupProductCode() {
    }
    
    public final void saveEntry() {
    }
    
    private final com.armada.expiryapp.data.db.entity.ExpiryEntry buildPendingEntry() {
        return null;
    }
    
    private final void runValidationPipeline(com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
    }
    
    private final void checkPastDate(com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
    }
    
    private final void checkQty(com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
    }
    
    private final void commitSave(com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
    }
    
    public final void onDuplicateConfirmMerge(long existingId, int additionalQty) {
    }
    
    public final void onDuplicateDismiss() {
    }
    
    public final void onPastDateConfirmed(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
    }
    
    public final void onPastDateDismiss() {
    }
    
    public final void onQtyConfirmed(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
    }
    
    public final void onQtyDismiss() {
    }
    
    public final void deleteEntry(long id) {
    }
    
    public final void undoDelete() {
    }
    
    public final void clearPendingDelete() {
    }
    
    public final void startEditEntry(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry) {
    }
    
    public final void cancelEdit() {
    }
    
    public final void saveEditedEntry(@org.jetbrains.annotations.NotNull()
    java.lang.String newExpiryIso, int newQty, @org.jetbrains.annotations.NotNull()
    java.lang.String newUnit) {
    }
    
    private final void clearEntry() {
    }
    
    private final void clearEntryForBulk() {
    }
    
    public final void activateBulkMode() {
    }
    
    public final void deactivateBulkMode() {
    }
    
    public final void onOcrDateScanned(@org.jetbrains.annotations.NotNull()
    java.lang.String rawDigits) {
    }
    
    public final void setExpiryDateFromCalendar(@org.jetbrains.annotations.NotNull()
    java.lang.String rawDigits) {
    }
    
    public final void onDescriptionItemSelected(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.Item item) {
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.FlowPreview.class})
    private final void startDescriptionSearch() {
    }
    
    private final boolean isDateValid(java.lang.String raw) {
        return false;
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$ActiveField;", "", "<init>", "(Ljava/lang/String;I)V", "NONE", "BARCODE", "DATE", "QTY", "app_debug"})
    public static enum ActiveField {
        /*public static final*/ NONE /* = new NONE() */,
        /*public static final*/ BARCODE /* = new BARCODE() */,
        /*public static final*/ DATE /* = new DATE() */,
        /*public static final*/ QTY /* = new QTY() */;
        
        ActiveField() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t\u00a8\u0006\u0016"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$BulkItem;", "", "barcode", "", "description", "productCode", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBarcode", "()Ljava/lang/String;", "getDescription", "getProductCode", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class BulkItem {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String barcode = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String description = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String productCode = null;
        
        public BulkItem(@org.jetbrains.annotations.NotNull()
        java.lang.String barcode, @org.jetbrains.annotations.NotNull()
        java.lang.String description, @org.jetbrains.annotations.NotNull()
        java.lang.String productCode) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getBarcode() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDescription() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getProductCode() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.BulkItem copy(@org.jetbrains.annotations.NotNull()
        java.lang.String barcode, @org.jetbrains.annotations.NotNull()
        java.lang.String description, @org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bR\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$Companion;", "", "<init>", "()V", "TIMESTAMP_FMT", "Ljava/time/format/DateTimeFormatter;", "kotlin.jvm.PlatformType", "formatDateDisplay", "", "raw", "rawToIso", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String formatDateDisplay(@org.jetbrains.annotations.NotNull()
        java.lang.String raw) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String rawToIso(@org.jetbrains.annotations.NotNull()
        java.lang.String raw) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0004\u0005\u0006\u0007B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0004\b\t\n\u000b\u00a8\u0006\f"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState;", "", "<init>", "()V", "None", "DuplicateFound", "PastDateWarning", "QtyWarning", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$DuplicateFound;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$None;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$PastDateWarning;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$QtyWarning;", "app_debug"})
    public static abstract class SaveDialogState {
        
        private SaveDialogState() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$DuplicateFound;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState;", "existing", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "pending", "<init>", "(Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;)V", "getExisting", "()Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "getPending", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class DuplicateFound extends com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState {
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.data.db.entity.ExpiryEntry existing = null;
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.data.db.entity.ExpiryEntry pending = null;
            
            public DuplicateFound(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry existing, @org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry getExisting() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry getPending() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState.DuplicateFound copy(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry existing, @org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
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
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$None;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState;", "<init>", "()V", "app_debug"})
        public static final class None extends com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState.None INSTANCE = null;
            
            private None() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$PastDateWarning;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState;", "pending", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "<init>", "(Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;)V", "getPending", "()Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class PastDateWarning extends com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState {
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.data.db.entity.ExpiryEntry pending = null;
            
            public PastDateWarning(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry getPending() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState.PastDateWarning copy(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
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
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState$QtyWarning;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$SaveDialogState;", "pending", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "<init>", "(Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;)V", "getPending", "()Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class QtyWarning extends com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState {
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.data.db.entity.ExpiryEntry pending = null;
            
            public QtyWarning(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry getPending() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.ExpiryEntry component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState.QtyWarning copy(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.ExpiryEntry pending) {
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
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007\u00a8\u0006\b"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState;", "", "<init>", "()V", "None", "Warning", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState$None;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState$Warning;", "app_debug"})
    public static abstract class UnlinkedItemState {
        
        private UnlinkedItemState() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState$None;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState;", "<init>", "()V", "app_debug"})
        public static final class None extends com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState.None INSTANCE = null;
            
            private None() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState$Warning;", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$UnlinkedItemState;", "item", "Lcom/armada/expiryapp/data/db/entity/Item;", "<init>", "(Lcom/armada/expiryapp/data/db/entity/Item;)V", "getItem", "()Lcom/armada/expiryapp/data/db/entity/Item;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class Warning extends com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState {
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.data.db.entity.Item item = null;
            
            public Warning(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.Item item) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.Item getItem() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.data.db.entity.Item component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState.Warning copy(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.data.db.entity.Item item) {
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
}