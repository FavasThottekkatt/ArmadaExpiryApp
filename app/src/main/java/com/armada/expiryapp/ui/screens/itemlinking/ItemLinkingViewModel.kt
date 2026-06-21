package com.armada.expiryapp.ui.screens.itemlinking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.data.db.entity.OutletItemLink
import com.armada.expiryapp.data.db.entity.TeamLink
import com.armada.expiryapp.data.repository.DeviceLockRepository
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.OutletItemLinkRepository
import com.armada.expiryapp.data.repository.TeamLinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemLinkingViewModel @Inject constructor(
    private val deviceLockRepository: DeviceLockRepository,
    private val teamLinkRepository:   TeamLinkRepository,
    private val itemRepository:       ItemRepository,
    private val linkRepository:       OutletItemLinkRepository,
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, t ->
        _snackMessage.tryEmit("Error: ${t.localizedMessage ?: "Unknown"}")
    }

    // ── Linked outlets for this device ────────────────────────────────────────

    private val _linkedOutlets = MutableStateFlow<List<TeamLink>>(emptyList())
    val linkedOutlets: StateFlow<List<TeamLink>> = _linkedOutlets.asStateFlow()

    // ── Selected outlet ───────────────────────────────────────────────────────

    private val _selectedOutletCode = MutableStateFlow("")
    private val _selectedOutletName = MutableStateFlow("")
    val selectedOutletCode: StateFlow<String> = _selectedOutletCode.asStateFlow()
    val selectedOutletName: StateFlow<String> = _selectedOutletName.asStateFlow()

    // ── Item search ───────────────────────────────────────────────────────────

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Set of barcodes currently linked to the selected outlet
    private val _linkedBarcodes = MutableStateFlow<Set<String>>(emptySet())
    val linkedBarcodes: StateFlow<Set<String>> = _linkedBarcodes.asStateFlow()

    private val _showClearDialog = MutableStateFlow(false)
    val showClearDialog: StateFlow<Boolean> = _showClearDialog.asStateFlow()

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val items: Flow<PagingData<Item>> = _searchQuery
        .debounce(150L)
        .flatMapLatest { query ->
            Pager(PagingConfig(pageSize = 25, enablePlaceholders = false, prefetchDistance = 5)) {
                if (query.isBlank()) itemRepository.getAllPaged()
                else itemRepository.searchByDescriptionOrCode(query)
            }.flow
        }
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val lock = deviceLockRepository.get()
            if (lock != null) {
                _linkedOutlets.value = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
            }
        }
    }

    // ── Outlet selection ──────────────────────────────────────────────────────

    fun selectOutlet(outletCode: String, outletName: String) {
        _selectedOutletCode.value = outletCode
        _selectedOutletName.value = outletName
        loadLinkedBarcodes(outletCode)
    }

    private fun loadLinkedBarcodes(outletCode: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val links = linkRepository.getAllForOutlet(outletCode)
            _linkedBarcodes.value = links.map { it.barcode }.toSet()
        }
    }

    // ── Item actions ──────────────────────────────────────────────────────────

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun toggleLink(item: Item) {
        val outletCode = _selectedOutletCode.value
        if (outletCode.isBlank()) return
        val barcode = item.barcode
        val linked  = barcode in _linkedBarcodes.value
        viewModelScope.launch(Dispatchers.IO + handler) {
            if (linked) {
                linkRepository.deleteByOutletAndBarcode(outletCode, barcode)
                _linkedBarcodes.update { it - barcode }
            } else {
                linkRepository.insert(
                    OutletItemLink(
                        outletCode  = outletCode,
                        barcode     = barcode,
                        description = item.description,
                        productCode = item.productCode,
                    )
                )
                _linkedBarcodes.update { it + barcode }
            }
        }
    }

    fun linkSelected() {
        val outletCode = _selectedOutletCode.value
        val outletName = _selectedOutletName.value
        if (outletCode.isBlank()) return
        val count = _linkedBarcodes.value.size
        if (count == 0) {
            _snackMessage.tryEmit("Please select items first by tapping them.")
        } else {
            _snackMessage.tryEmit("$count items linked to $outletName.")
        }
    }

    fun requestClearAll() { _showClearDialog.value = true }
    fun dismissClearAll() { _showClearDialog.value = false }
    fun confirmClearAll() {
        val outletCode = _selectedOutletCode.value
        if (outletCode.isBlank()) return
        _showClearDialog.value = false
        viewModelScope.launch(Dispatchers.IO + handler) {
            linkRepository.deleteAllForOutlet(outletCode)
            _linkedBarcodes.value = emptySet()
            _snackMessage.tryEmit("All item links cleared.")
        }
    }
}
