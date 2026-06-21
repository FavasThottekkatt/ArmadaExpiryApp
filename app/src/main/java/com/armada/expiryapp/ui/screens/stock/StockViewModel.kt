package com.armada.expiryapp.ui.screens.stock

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.data.db.entity.StockEntry
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.StockEntryRepository
import com.armada.expiryapp.data.session.SessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val sessionHolder: SessionHolder,
    private val stockRepository: StockEntryRepository,
    private val itemRepository: ItemRepository,
) : ViewModel() {

    data class StockState(
        val stockEntryId: Long = 0L,
        val isOos: Boolean = false,
        val quantity: Int = 0,
        val unit: String = "PC",
        val description: String = "",
        val productCode: String? = null,
    )

    private val handler = CoroutineExceptionHandler { _, t ->
        _snackMessage.tryEmit("Error: ${t.localizedMessage ?: "Unknown"}")
    }

    val hasOutlet: Boolean get() = sessionHolder.outletCode.isNotBlank()

    // ── Stock state map (barcode → state) ─────────────────────────────────────

    private val _stockMap = MutableStateFlow<Map<String, StockState>>(emptyMap())
    val stockMap: StateFlow<Map<String, StockState>> = _stockMap.asStateFlow()

    // ── Search ────────────────────────────────────────────────────────────────

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ── Focused barcode (which row the numpad targets) ────────────────────────

    private val _focusedBarcode = MutableStateFlow<String?>(null)
    val focusedBarcode: StateFlow<String?> = _focusedBarcode.asStateFlow()

    // ── Selected unit (for current focused row) ───────────────────────────────

    private val _selectedUnit = MutableStateFlow("PC")
    val selectedUnit: StateFlow<String> = _selectedUnit.asStateFlow()

    // ── Dialogs ───────────────────────────────────────────────────────────────

    private val _showClearAllDialog = MutableStateFlow(false)
    val showClearAllDialog: StateFlow<Boolean> = _showClearAllDialog.asStateFlow()

    // ── Events ────────────────────────────────────────────────────────────────

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    private val _shareText = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val shareText: SharedFlow<String> = _shareText.asSharedFlow()

    // ── Paging items (full items list, filtered by search) ────────────────────

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val items: Flow<PagingData<Item>> = _searchQuery
        .debounce(150L)
        .flatMapLatest { query ->
            if (!hasOutlet) return@flatMapLatest flowOf(PagingData.empty())
            Pager(PagingConfig(pageSize = 25, enablePlaceholders = false)) {
                if (query.isBlank()) itemRepository.getAllPaged()
                else itemRepository.searchByDescriptionOrCode(query)
            }.flow
        }
        .cachedIn(viewModelScope)

    // ── Init ──────────────────────────────────────────────────────────────────

    init {
        if (hasOutlet) loadStockState()
    }

    private fun loadStockState() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val entries = stockRepository.getActiveEntries(
                sessionHolder.outletCode,
                sessionHolder.merchandiser,
                sessionHolder.salesman,
            )
            _stockMap.value = entries.associate { e ->
                e.barcode to StockState(
                    stockEntryId = e.id,
                    isOos        = e.isOos,
                    quantity     = e.quantity,
                    unit         = e.unit,
                    description  = e.description,
                    productCode  = e.productCode,
                )
            }
        }
    }

    // ── Search ────────────────────────────────────────────────────────────────

    fun setSearchQuery(q: String) { _searchQuery.value = q }

    fun clearSearch() { _searchQuery.value = "" }

    // ── OOS toggle ────────────────────────────────────────────────────────────

    fun toggleOos(barcode: String, description: String, productCode: String?) {
        val newOos = !(_stockMap.value[barcode]?.isOos ?: false)
        _stockMap.update { map ->
            val cur = map[barcode] ?: StockState(description = description, productCode = productCode)
            map + (barcode to cur.copy(
                isOos       = newOos,
                quantity    = if (newOos) 0 else cur.quantity,
                description = description,
                productCode = productCode,
            ))
        }
        if (newOos && _focusedBarcode.value == barcode) _focusedBarcode.value = null

        val state = _stockMap.value[barcode] ?: return
        if (!state.isOos && state.quantity == 0) {
            if (state.stockEntryId > 0L) {
                viewModelScope.launch(Dispatchers.IO + handler) {
                    stockRepository.deleteById(state.stockEntryId)
                }
                _stockMap.update { it - barcode }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO + handler) { upsertState(barcode, state) }
        }
    }

    // ── QTY field tap ─────────────────────────────────────────────────────────

    fun tapQtyField(barcode: String, description: String, productCode: String?) {
        val current = _stockMap.value[barcode]
        when {
            _focusedBarcode.value != barcode -> {
                _focusedBarcode.value = barcode
                if (current == null) {
                    _stockMap.update { map ->
                        map + (barcode to StockState(
                            description = description,
                            productCode = productCode,
                            unit = _selectedUnit.value,
                        ))
                    }
                }
            }
            (current?.quantity ?: 0) > 0 -> {
                // Tap on focused cell with value → clear
                _stockMap.update { map ->
                    val cur = map[barcode] ?: return@update map
                    map + (barcode to cur.copy(quantity = 0))
                }
                val state = _stockMap.value[barcode]
                if (state != null && !state.isOos && state.stockEntryId > 0L) {
                    viewModelScope.launch(Dispatchers.IO + handler) {
                        stockRepository.deleteById(state.stockEntryId)
                    }
                    _stockMap.update { it - barcode }
                }
            }
            else -> _focusedBarcode.value = null
        }
    }

    // ── Numpad digit ──────────────────────────────────────────────────────────

    fun stockClearSingle() {
        val barcode = _focusedBarcode.value ?: return
        _stockMap.update { map ->
            val cur = map[barcode] ?: return@update map
            map + (barcode to cur.copy(quantity = cur.quantity / 10))
        }
        val state = _stockMap.value[barcode] ?: return
        viewModelScope.launch(Dispatchers.IO + handler) { upsertState(barcode, state) }
    }

    fun stockClearAll() {
        val barcode = _focusedBarcode.value ?: return
        val state   = _stockMap.value[barcode]
        _stockMap.update { map ->
            val cur = map[barcode] ?: return@update map
            map + (barcode to cur.copy(quantity = 0))
        }
        if (state != null && !state.isOos && state.stockEntryId > 0L) {
            viewModelScope.launch(Dispatchers.IO + handler) {
                stockRepository.deleteById(state.stockEntryId)
            }
            _stockMap.update { it - barcode }
        }
    }

    fun appendDigit(digit: Int) {
        val barcode = _focusedBarcode.value ?: return
        _stockMap.update { map ->
            val cur = map[barcode] ?: return@update map
            val newQty = cur.quantity * 10 + digit
            if (newQty > 9999) return@update map
            map + (barcode to cur.copy(quantity = newQty))
        }
        val state = _stockMap.value[barcode] ?: return
        viewModelScope.launch(Dispatchers.IO + handler) { upsertState(barcode, state) }
    }

    // ── Unit selector ─────────────────────────────────────────────────────────

    fun setUnit(unit: String) {
        _selectedUnit.value = unit
        val barcode = _focusedBarcode.value ?: return
        _stockMap.update { map ->
            val cur = map[barcode] ?: return@update map
            map + (barcode to cur.copy(unit = unit))
        }
        val state = _stockMap.value[barcode] ?: return
        if (state.isOos || state.quantity > 0) {
            viewModelScope.launch(Dispatchers.IO + handler) { upsertState(barcode, state) }
        }
    }

    // ── Share report ──────────────────────────────────────────────────────────

    fun shareReport() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val entries = stockRepository.getActiveEntries(
                sessionHolder.outletCode,
                sessionHolder.merchandiser,
                sessionHolder.salesman,
            )
            val lowStock = entries.filter { it.quantity > 0 && !it.isOos }
                .sortedBy { it.description }
            val oos = entries.filter { it.isOos }
                .sortedBy { it.description }

            if (lowStock.isEmpty() && oos.isEmpty()) {
                _snackMessage.tryEmit("No items marked. Mark items before sharing.")
                return@launch
            }

            val now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMMM yyyy  hh:mm a", Locale.getDefault()))
            val sb = StringBuilder()
            sb.append("*${sessionHolder.outletName}*").append("\n")
            sb.append(now).append("\n\n")

            if (lowStock.isNotEmpty()) {
                sb.append("*LOW STOCK*\n")
                lowStock.forEach { e -> sb.append("${e.description} - ${e.quantity} ${e.unit}\n") }
            }

            if (lowStock.isNotEmpty() && oos.isNotEmpty()) sb.append("\n")

            if (oos.isNotEmpty()) {
                sb.append("*OUT OF STOCK*\n")
                oos.forEach { e -> sb.append("${e.description}\n") }
            }

            _shareText.tryEmit(sb.toString().trimEnd())
        }
    }

    // ── Clear all ─────────────────────────────────────────────────────────────

    fun requestClearAll() { _showClearAllDialog.value = true }

    fun dismissClearAll() { _showClearAllDialog.value = false }

    fun confirmClearAll() {
        _showClearAllDialog.value = false
        viewModelScope.launch(Dispatchers.IO + handler) {
            stockRepository.deleteAllForSession(
                sessionHolder.outletCode,
                sessionHolder.merchandiser,
                sessionHolder.salesman,
            )
            _stockMap.value = emptyMap()
            _focusedBarcode.value = null
            _snackMessage.tryEmit("All stock entries cleared.")
        }
    }

    // ── Persist helper ────────────────────────────────────────────────────────

    private suspend fun upsertState(barcode: String, state: StockState) {
        val entry = StockEntry(
            id             = state.stockEntryId,
            barcode        = barcode,
            description    = state.description,
            productCode    = state.productCode,
            isOos          = state.isOos,
            quantity       = state.quantity,
            unit           = state.unit,
            outletName     = sessionHolder.outletName,
            outletCode     = sessionHolder.outletCode,
            merchandiser   = sessionHolder.merchandiser,
            salesman       = sessionHolder.salesman,
            entryTimestamp = LocalDateTime.now().toString(),
            archived       = false,
        )
        val returnedId = stockRepository.upsert(entry)
        if (state.stockEntryId == 0L && returnedId > 0L) {
            _stockMap.update { map ->
                val cur = map[barcode] ?: return@update map
                map + (barcode to cur.copy(stockEntryId = returnedId))
            }
        }
    }
}
