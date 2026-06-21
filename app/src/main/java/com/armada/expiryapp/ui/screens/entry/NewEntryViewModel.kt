package com.armada.expiryapp.ui.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.repository.ExpiryEntryRepository
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.OutletItemLinkRepository
import com.armada.expiryapp.data.session.SessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import com.armada.expiryapp.data.db.entity.Item
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NewEntryViewModel @Inject constructor(
    val sessionHolder:   SessionHolder,
    private val entryRepository: ExpiryEntryRepository,
    private val itemRepository:  ItemRepository,
    private val linkRepository:  OutletItemLinkRepository,
) : ViewModel() {

    enum class ActiveField { NONE, BARCODE, DATE, QTY }

    sealed class SaveDialogState {
        object None : SaveDialogState()
        data class DuplicateFound(
            val existing: ExpiryEntry,
            val pending:  ExpiryEntry,
        ) : SaveDialogState()
        data class PastDateWarning(val pending: ExpiryEntry) : SaveDialogState()
        data class QtyWarning(val pending: ExpiryEntry) : SaveDialogState()
    }

    sealed class UnlinkedItemState {
        object None : UnlinkedItemState()
        data class Warning(val item: Item) : UnlinkedItemState()
    }

    private data class BulkItem(val barcode: String, val description: String, val productCode: String)

    private val handler = CoroutineExceptionHandler { _, t ->
        _snackMessage.tryEmit("Error: ${t.localizedMessage ?: "Unknown error"}")
    }

    // ── Item state ────────────────────────────────────────────────────────────

    private val _barcode          = MutableStateFlow("")
    private val _description      = MutableStateFlow("")
    private val _productCode      = MutableStateFlow("")
    private val _descriptionQuery = MutableStateFlow("")
    private val _productCodeQuery = MutableStateFlow("")
    private val _itemFilled       = MutableStateFlow(false)

    // ── Bulk Mode state ───────────────────────────────────────────────────────

    private val _isBulkMode    = MutableStateFlow(false)
    private val _lastSavedItem = MutableStateFlow<BulkItem?>(null)

    val isBulkMode: StateFlow<Boolean> = _isBulkMode.asStateFlow()

    val showRepeatButton: StateFlow<Boolean> = combine(
        _isBulkMode, _lastSavedItem,
    ) { bulkMode, lastItem -> !bulkMode && lastItem != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // ── Item state (exposed) ──────────────────────────────────────────────────

    val barcode:          StateFlow<String>  = _barcode.asStateFlow()
    val description:      StateFlow<String>  = _description.asStateFlow()
    val productCode:      StateFlow<String>  = _productCode.asStateFlow()
    val descriptionQuery: StateFlow<String>  = _descriptionQuery.asStateFlow()
    val productCodeQuery: StateFlow<String>  = _productCodeQuery.asStateFlow()
    val itemFilled:       StateFlow<Boolean> = _itemFilled.asStateFlow()

    // ── Description search results ────────────────────────────────────────────

    private val _descriptionResults = MutableStateFlow<List<Item>>(emptyList())
    private val _isSearching        = MutableStateFlow(false)
    val descriptionResults: StateFlow<List<Item>> = _descriptionResults.asStateFlow()
    val isSearching:        StateFlow<Boolean>    = _isSearching.asStateFlow()

    // ── Date state (numpad-driven, raw digits dd mm yyyy) ─────────────────────

    private val _expiryDateRaw = MutableStateFlow("")
    val expiryDateRaw: StateFlow<String> = _expiryDateRaw.asStateFlow()

    val expiryDateDisplay: StateFlow<String> = _expiryDateRaw
        .map { formatDateDisplay(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val isDateComplete: StateFlow<Boolean> = _expiryDateRaw
        .map { it.length == 8 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // ── Quantity & unit state ─────────────────────────────────────────────────

    private val _quantity = MutableStateFlow("")
    private val _unit     = MutableStateFlow("PC")

    val quantity: StateFlow<String> = _quantity.asStateFlow()
    val unit:     StateFlow<String> = _unit.asStateFlow()

    // ── Active field ──────────────────────────────────────────────────────────

    private val _activeField = MutableStateFlow(ActiveField.NONE)
    val activeField: StateFlow<ActiveField> = _activeField.asStateFlow()

    // ── Validation dialogs ────────────────────────────────────────────────────

    private val _saveDialogState = MutableStateFlow<SaveDialogState>(SaveDialogState.None)
    val saveDialogState: StateFlow<SaveDialogState> = _saveDialogState.asStateFlow()

    // ── Unlinked item warning (barcode scan) ──────────────────────────────────

    private val _unlinkedItemState = MutableStateFlow<UnlinkedItemState>(UnlinkedItemState.None)
    val unlinkedItemState: StateFlow<UnlinkedItemState> = _unlinkedItemState.asStateFlow()

    // ── Edit dialog ───────────────────────────────────────────────────────────

    private val _editingEntry = MutableStateFlow<ExpiryEntry?>(null)
    val editingEntry: StateFlow<ExpiryEntry?> = _editingEntry.asStateFlow()

    // ── Undo delete ───────────────────────────────────────────────────────────

    @Volatile private var lastDeletedEntry: ExpiryEntry? = null

    // ── Snackbar (one-shot) ───────────────────────────────────────────────────

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    // ── Latest entries (Paging 3) — lazy so SessionHolder is set first ────────

    val latestEntries: Flow<PagingData<ExpiryEntry>> by lazy {
        Pager(PagingConfig(pageSize = 25, enablePlaceholders = false, prefetchDistance = 5)) {
            entryRepository.getActiveEntriesPaged(sessionHolder.outletCode)
        }.flow.cachedIn(viewModelScope)
    }

    init {
        startDescriptionSearch()
    }

    // ── Numpad actions ────────────────────────────────────────────────────────

    fun onNumpadDigit(digit: Int) {
        when (_activeField.value) {
            ActiveField.BARCODE -> {
                if (_barcode.value.length < 20) _barcode.value += digit.toString()
            }
            ActiveField.DATE -> {
                val raw = _expiryDateRaw.value
                if (raw.length < 8) {
                    _expiryDateRaw.value = raw + digit.toString()
                    if (_expiryDateRaw.value.length == 8) {
                        _activeField.value = ActiveField.QTY
                    }
                }
            }
            ActiveField.QTY -> {
                if (_quantity.value.length < 5) _quantity.value += digit.toString()
            }
            ActiveField.NONE -> {}
        }
    }

    fun onNumpadClearSingle() {
        when (_activeField.value) {
            ActiveField.BARCODE -> _barcode.value         = _barcode.value.dropLast(1)
            ActiveField.DATE    -> _expiryDateRaw.value   = _expiryDateRaw.value.dropLast(1)
            ActiveField.QTY     -> _quantity.value        = _quantity.value.dropLast(1)
            ActiveField.NONE    -> {}
        }
    }

    fun onNumpadClearAll() {
        when (_activeField.value) {
            ActiveField.BARCODE -> _barcode.value       = ""
            ActiveField.DATE    -> _expiryDateRaw.value = ""
            ActiveField.QTY     -> _quantity.value      = ""
            ActiveField.NONE    -> {}
        }
    }

    // ── Field actions ─────────────────────────────────────────────────────────

    fun setActiveField(field: ActiveField)        { _activeField.value      = field }

    fun setUnit(unit: String) {
        _unit.value = unit
        // Tapping PC / OUT / CTN is the save trigger — not auto-save
        val raw = _expiryDateRaw.value
        val qty = _quantity.value.toIntOrNull() ?: 0
        when {
            !_itemFilled.value -> _snackMessage.tryEmit("Please select an item first.")
            raw.length < 8     -> _snackMessage.tryEmit("Please enter a complete expiry date.")
            qty <= 0           -> _snackMessage.tryEmit("Please enter a quantity first.")
            else               -> saveEntry()
        }
    }

    fun setDescriptionQuery(query: String)        { _descriptionQuery.value = query }
    fun setProductCodeQuery(query: String)        { _productCodeQuery.value = query }

    fun clearItem() {
        _barcode.value            = ""
        _description.value        = ""
        _productCode.value        = ""
        _itemFilled.value         = false
        _descriptionQuery.value   = ""
        _productCodeQuery.value   = ""
        _descriptionResults.value = emptyList()
        _isSearching.value        = false
        _activeField.value        = ActiveField.BARCODE
    }

    // ── Lookups ───────────────────────────────────────────────────────────────

    fun lookupBarcode() {
        val bc = _barcode.value.trim()
        if (bc.isBlank()) return
        viewModelScope.launch(Dispatchers.IO + handler) {
            fillFromBarcode(bc)
        }
    }

    fun onBarcodeScanned(rawBarcode: String) {
        val bc = rawBarcode.trim()
        if (bc.isBlank()) return
        _barcode.value     = bc
        _activeField.value = ActiveField.BARCODE
        viewModelScope.launch(Dispatchers.IO + handler) {
            fillFromBarcode(bc)
        }
    }

    private suspend fun fillFromBarcode(bc: String) {
        val item = itemRepository.findByBarcode(bc)
        if (item == null) {
            _snackMessage.tryEmit("Barcode not found. Enter description manually.")
            return
        }
        val outletCode = sessionHolder.outletCode
        if (outletCode.isNotBlank()) {
            val count = linkRepository.getCountForOutlet(outletCode)
            if (count > 0 && !linkRepository.isLinked(outletCode, bc)) {
                _unlinkedItemState.value = UnlinkedItemState.Warning(item)
                return
            }
        }
        applyItem(item)
    }

    private fun applyItem(item: Item) {
        _barcode.value     = item.barcode
        _description.value = item.description
        _productCode.value = item.productCode ?: ""
        _itemFilled.value  = true
        _activeField.value = ActiveField.DATE
    }

    fun onUnlinkedItemConfirm() {
        val warning = _unlinkedItemState.value as? UnlinkedItemState.Warning ?: return
        _unlinkedItemState.value = UnlinkedItemState.None
        applyItem(warning.item)
    }

    fun onUnlinkedItemDismiss() {
        _unlinkedItemState.value = UnlinkedItemState.None
        _barcode.value = ""
    }

    fun lookupProductCode() {
        val code = _productCodeQuery.value.trim()
        if (code.isBlank()) return
        viewModelScope.launch(Dispatchers.IO + handler) {
            val item = itemRepository.findByProductCode(code)
            if (item != null) {
                _barcode.value          = item.barcode
                _description.value      = item.description
                _productCode.value      = item.productCode ?: ""
                _itemFilled.value       = true
                _productCodeQuery.value = ""
                _activeField.value      = ActiveField.DATE
            } else {
                _snackMessage.tryEmit("Product code '${code}' not found.")
            }
        }
    }

    // ── Save — validation pipeline ────────────────────────────────────────────

    fun saveEntry() {
        val raw = _expiryDateRaw.value
        val qty = _quantity.value.toIntOrNull() ?: 0
        when {
            !_itemFilled.value -> { _snackMessage.tryEmit("Please select an item first."); return }
            raw.length < 8     -> { _snackMessage.tryEmit("Please enter a complete expiry date."); return }
            !isDateValid(raw)  -> { _snackMessage.tryEmit("Invalid expiry date. Check day and month."); return }
            qty <= 0           -> { _snackMessage.tryEmit("Please enter a valid quantity."); return }
        }
        runValidationPipeline(buildPendingEntry())
    }

    private fun buildPendingEntry(): ExpiryEntry {
        val raw = _expiryDateRaw.value
        return ExpiryEntry(
            barcode        = _barcode.value,
            description    = _description.value,
            productCode    = _productCode.value.ifBlank { null },
            expiryDate     = rawToIso(raw),
            quantity       = _quantity.value.toIntOrNull() ?: 0,
            unit           = _unit.value,
            outletName     = sessionHolder.outletName,
            outletCode     = sessionHolder.outletCode,
            merchandiser   = sessionHolder.merchandiser,
            salesman       = sessionHolder.salesman,
            entryTimestamp = LocalDateTime.now().format(TIMESTAMP_FMT),
        )
    }

    private fun runValidationPipeline(pending: ExpiryEntry) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val duplicate = entryRepository.findDuplicate(
                pending.barcode, pending.outletCode, pending.expiryDate, pending.merchandiser,
            )
            if (duplicate != null) {
                _saveDialogState.value = SaveDialogState.DuplicateFound(duplicate, pending)
                return@launch
            }
            checkPastDate(pending)
        }
    }

    private fun checkPastDate(pending: ExpiryEntry) {
        val isPast = runCatching {
            LocalDate.parse(pending.expiryDate).isBefore(LocalDate.now())
        }.getOrDefault(false)
        if (isPast) { _saveDialogState.value = SaveDialogState.PastDateWarning(pending); return }
        checkQty(pending)
    }

    private fun checkQty(pending: ExpiryEntry) {
        if (pending.quantity > 20) { _saveDialogState.value = SaveDialogState.QtyWarning(pending); return }
        commitSave(pending)
    }

    private fun commitSave(pending: ExpiryEntry) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            entryRepository.insert(pending)
            _lastSavedItem.value = BulkItem(
                barcode     = pending.barcode,
                description = pending.description,
                productCode = pending.productCode ?: "",
            )
            if (_isBulkMode.value) clearEntryForBulk() else clearEntry()
        }
    }

    // ── Dialog callbacks ──────────────────────────────────────────────────────

    fun onDuplicateConfirmMerge(existingId: Long, additionalQty: Int) {
        _saveDialogState.value = SaveDialogState.None
        viewModelScope.launch(Dispatchers.IO + handler) {
            entryRepository.incrementQuantity(existingId, additionalQty)
            clearEntry()
            _snackMessage.tryEmit("Quantity updated on existing entry.")
        }
    }

    fun onDuplicateDismiss()                         { _saveDialogState.value = SaveDialogState.None }

    fun onPastDateConfirmed(pending: ExpiryEntry) {
        _saveDialogState.value = SaveDialogState.None
        checkQty(pending)
    }

    fun onPastDateDismiss()                          { _saveDialogState.value = SaveDialogState.None }

    fun onQtyConfirmed(pending: ExpiryEntry) {
        _saveDialogState.value = SaveDialogState.None
        commitSave(pending)
    }

    fun onQtyDismiss()                               { _saveDialogState.value = SaveDialogState.None }

    // ── Delete + undo ─────────────────────────────────────────────────────────

    fun deleteEntry(id: Long) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            lastDeletedEntry = entryRepository.findById(id)
            entryRepository.deleteById(id)
        }
    }

    fun undoDelete() {
        val entry = lastDeletedEntry ?: return
        lastDeletedEntry = null
        viewModelScope.launch(Dispatchers.IO + handler) {
            entryRepository.insert(entry.copy(id = 0))
        }
    }

    fun clearPendingDelete() { lastDeletedEntry = null }

    // ── Edit entry ────────────────────────────────────────────────────────────

    fun startEditEntry(entry: ExpiryEntry) { _editingEntry.value = entry }
    fun cancelEdit()                        { _editingEntry.value = null  }

    fun saveEditedEntry(newExpiryIso: String, newQty: Int, newUnit: String) {
        val current = _editingEntry.value ?: return
        _editingEntry.value = null
        viewModelScope.launch(Dispatchers.IO + handler) {
            entryRepository.update(current.copy(expiryDate = newExpiryIso, quantity = newQty, unit = newUnit))
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun clearEntry() {
        _barcode.value            = ""
        _description.value        = ""
        _productCode.value        = ""
        _descriptionQuery.value   = ""
        _productCodeQuery.value   = ""
        _descriptionResults.value = emptyList()
        _isSearching.value        = false
        _itemFilled.value         = false
        _expiryDateRaw.value      = ""
        _quantity.value           = ""
        _unit.value               = "PC"
        _activeField.value        = ActiveField.BARCODE
    }

    private fun clearEntryForBulk() {
        val item = _lastSavedItem.value ?: run { clearEntry(); return }
        _barcode.value            = item.barcode
        _description.value        = item.description
        _productCode.value        = item.productCode
        _itemFilled.value         = true
        _descriptionQuery.value   = ""
        _productCodeQuery.value   = ""
        _descriptionResults.value = emptyList()
        _isSearching.value        = false
        _expiryDateRaw.value      = ""
        _quantity.value           = ""
        _unit.value               = "PC"
        _activeField.value        = ActiveField.DATE
    }

    fun activateBulkMode() {
        _isBulkMode.value = true
        clearEntryForBulk()
    }

    fun deactivateBulkMode() {
        _isBulkMode.value = false
        clearEntry()
    }

    fun onOcrDateScanned(rawDigits: String) {
        if (rawDigits.length != 8) return
        _expiryDateRaw.value = rawDigits
        _activeField.value   = ActiveField.QTY
    }

    fun setExpiryDateFromCalendar(rawDigits: String) {
        if (rawDigits.length != 8) return
        _expiryDateRaw.value = rawDigits
        _activeField.value   = ActiveField.QTY
    }

    fun onDescriptionItemSelected(item: Item) {
        applyItem(item)
        _descriptionQuery.value   = ""
        _descriptionResults.value = emptyList()
        _isSearching.value        = false
    }

    @OptIn(FlowPreview::class)
    private fun startDescriptionSearch() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _descriptionQuery
                .debounce(150L)
                .collect { query ->
                    if (query.length < 2) {
                        _descriptionResults.value = emptyList()
                        _isSearching.value        = false
                    } else {
                        _isSearching.value = true
                        val outletCode = sessionHolder.outletCode
                        _descriptionResults.value =
                            if (outletCode.isNotBlank() &&
                                linkRepository.getCountForOutlet(outletCode) > 0
                            ) {
                                linkRepository.searchForDropdown(outletCode, query).map { link ->
                                    Item(barcode = link.barcode, description = link.description,
                                         productCode = link.productCode)
                                }
                            } else {
                                itemRepository.searchForDropdown(query)
                            }
                        _isSearching.value = false
                    }
                }
        }
    }

    private fun isDateValid(raw: String): Boolean {
        if (raw.length != 8) return false
        return runCatching {
            val dd   = raw.take(2).toInt()
            val mm   = raw.substring(2, 4).toInt()
            dd in 1..31 && mm in 1..12
        }.getOrDefault(false)
    }

    companion object {
        private val TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun formatDateDisplay(raw: String): String = when {
            raw.length <= 2 -> raw
            raw.length <= 4 -> "${raw.take(2)}/${raw.drop(2)}"
            else            -> "${raw.take(2)}/${raw.substring(2, 4)}/${raw.drop(4)}"
        }

        fun rawToIso(raw: String): String {
            val dd   = raw.take(2)
            val mm   = raw.substring(2, 4)
            val yyyy = raw.drop(4)
            return "$yyyy-$mm-$dd"
        }
    }
}
