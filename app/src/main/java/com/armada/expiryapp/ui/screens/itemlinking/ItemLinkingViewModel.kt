package com.armada.expiryapp.ui.screens.itemlinking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.data.db.entity.OutletItemLink
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.OutletItemLinkRepository
import com.armada.expiryapp.data.session.SessionHolder
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
    val sessionHolder:  SessionHolder,
    private val itemRepository: ItemRepository,
    private val linkRepository: OutletItemLinkRepository,
) : ViewModel() {

    val outletCode: String get() = sessionHolder.outletCode
    val outletName: String get() = sessionHolder.outletName

    private val handler = CoroutineExceptionHandler { _, t ->
        _snackMessage.tryEmit("Error: ${t.localizedMessage ?: "Unknown"}")
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Set of barcodes currently linked to this outlet
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
        loadLinkedBarcodes()
    }

    private fun loadLinkedBarcodes() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val links = linkRepository.getAllForOutlet(outletCode)
            _linkedBarcodes.value = links.map { it.barcode }.toSet()
        }
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun toggleLink(item: Item) {
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

    // Links all items matching the current search query (fetches fresh from DB — no paging limit)
    fun linkAllShown() {
        val query = _searchQuery.value
        viewModelScope.launch(Dispatchers.IO + handler) {
            val allMatching = if (query.isBlank()) itemRepository.getAll()
                              else itemRepository.searchAll(query)
            val toLink = allMatching.filter { it.barcode !in _linkedBarcodes.value }
            if (toLink.isEmpty()) { _snackMessage.tryEmit("All shown items are already linked."); return@launch }
            val newLinks = toLink.map {
                OutletItemLink(outletCode = outletCode, barcode = it.barcode,
                               description = it.description, productCode = it.productCode)
            }
            linkRepository.insertAll(newLinks)
            _linkedBarcodes.update { current -> current + toLink.map { it.barcode }.toSet() }
            _snackMessage.tryEmit("Linked ${toLink.size} item(s).")
        }
    }

    fun requestClearAll() { _showClearDialog.value = true }
    fun dismissClearAll() { _showClearDialog.value = false }
    fun confirmClearAll() {
        _showClearDialog.value = false
        viewModelScope.launch(Dispatchers.IO + handler) {
            linkRepository.deleteAllForOutlet(outletCode)
            _linkedBarcodes.value = emptySet()
            _snackMessage.tryEmit("All item links cleared.")
        }
    }
}
