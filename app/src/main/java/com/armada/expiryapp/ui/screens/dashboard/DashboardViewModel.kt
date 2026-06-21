package com.armada.expiryapp.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.data.repository.CsvMetadataRepository
import com.armada.expiryapp.data.repository.DeviceLockRepository
import com.armada.expiryapp.data.repository.ExpiryEntryRepository
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.OutletRepository
import com.armada.expiryapp.data.repository.TeamLinkRepository
import com.armada.expiryapp.data.session.SessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val outletRepository:      OutletRepository,
    private val entryRepository:       ExpiryEntryRepository,
    private val itemRepository:        ItemRepository,
    private val csvMetadataRepository: CsvMetadataRepository,
    private val sessionHolder:         SessionHolder,
    private val deviceLockRepository:  DeviceLockRepository,
    private val teamLinkRepository:    TeamLinkRepository,
) : ViewModel() {

    // ── Inner data classes ────────────────────────────────────────────────────

    data class DashboardStats(
        val expired:  Int = 0,
        val within30: Int = 0,
        val within60: Int = 0,
        val within90: Int = 0,
    )

    data class SessionRecovery(
        val outletName: String,
        val outletCode: String,
        val entryCount: Int,
    )

    data class MasterDataInfo(
        val itemCount:   Int,
        val outletCount: Int,
        val importedAt:  String,
    )

    // ── Error handler ─────────────────────────────────────────────────────────

    private val handler = CoroutineExceptionHandler { _, _ -> }

    // ── Team Linking state ────────────────────────────────────────────────────

    val isTeamLinkingComplete: StateFlow<Boolean> = deviceLockRepository.getFlow()
        .flatMapLatest { lock ->
            if (lock == null) flowOf(false)
            else teamLinkRepository.getCountFlow(lock.merchandiserName).map { it > 0 }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val teamLinks = deviceLockRepository.getFlow()
        .flatMapLatest { lock ->
            if (lock == null) flowOf(emptyList())
            else teamLinkRepository.getAllForMerchandiserFlow(lock.merchandiserName)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Internal merchandiser/salesman — auto-filled from DB, never shown in UI
    private val _currentMerchandiser = MutableStateFlow("")
    private val _currentSalesman     = MutableStateFlow("")

    // ── Form state ────────────────────────────────────────────────────────────

    private val _outletName    = MutableStateFlow("")
    val outletName: StateFlow<String> = _outletName.asStateFlow()

    private val _outletCode    = MutableStateFlow("")
    val outletCode: StateFlow<String> = _outletCode.asStateFlow()

    private val _outletQuery   = MutableStateFlow("")
    private val _outletResults = MutableStateFlow<List<Outlet>>(emptyList())
    val outletResults: StateFlow<List<Outlet>> = _outletResults.asStateFlow()

    private val _showFieldErrors = MutableStateFlow(false)
    val showFieldErrors: StateFlow<Boolean> = _showFieldErrors.asStateFlow()

    val isFormComplete: StateFlow<Boolean> = _outletName
        .map { it.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // ── Session recovery ──────────────────────────────────────────────────────

    private val _sessionRecovery = MutableStateFlow<SessionRecovery?>(null)
    val sessionRecovery: StateFlow<SessionRecovery?> = _sessionRecovery.asStateFlow()

    // ── Master data info ──────────────────────────────────────────────────────

    private val _masterDataInfo = MutableStateFlow<MasterDataInfo?>(null)
    val masterDataInfo: StateFlow<MasterDataInfo?> = _masterDataInfo.asStateFlow()

    // ── Dashboard stats ───────────────────────────────────────────────────────

    val stats: StateFlow<DashboardStats> = _outletCode
        .flatMapLatest { code ->
            if (code.isBlank()) {
                flowOf(DashboardStats())
            } else {
                val today = LocalDate.now().toString()
                val d30   = LocalDate.now().plusDays(30).toString()
                val d60   = LocalDate.now().plusDays(60).toString()
                val d90   = LocalDate.now().plusDays(90).toString()
                combine(
                    entryRepository.getExpiredCountFlow(code, today),
                    entryRepository.getWithin30CountFlow(code, today, d30),
                    entryRepository.getWithin60CountFlow(code, d30, d60),
                    entryRepository.getWithin90CountFlow(code, d60, d90),
                ) { e, w30, w60, w90 -> DashboardStats(e, w30, w60, w90) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, DashboardStats())

    // ── Paging ────────────────────────────────────────────────────────────────

    private val _rawSearchQuery       = MutableStateFlow("")
    private val _debouncedSearchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _rawSearchQuery.asStateFlow()

    val latestEntries: Flow<PagingData<ExpiryEntry>> = combine(
        _outletCode, _debouncedSearchQuery,
    ) { code, query -> code to query }
        .flatMapLatest { (code, query) ->
            if (code.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                Pager(PagingConfig(pageSize = 25, enablePlaceholders = false, prefetchDistance = 5)) {
                    if (query.isBlank()) entryRepository.getActiveEntriesPaged(code)
                    else entryRepository.searchActiveEntriesPaged(code, query)
                }.flow
            }
        }
        .cachedIn(viewModelScope)

    // ── Init ──────────────────────────────────────────────────────────────────

    init {
        startOutletSearch()
        startSearchDebounce()
        loadMasterDataInfo()
        checkSessionRecovery()
        loadLockedMerchandiser()
    }

    private fun startOutletSearch() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _outletQuery.debounce(150L).collect { query ->
                if (query.isBlank()) {
                    _outletResults.value = emptyList()
                    return@collect
                }
                // Search only within linked outlets for this merchandiser
                val links = teamLinks.value
                _outletResults.value = links
                    .filter { it.outletName.contains(query, ignoreCase = true) }
                    .take(30)
                    .map { Outlet(outletCode = it.outletCode, outletName = it.outletName, shortName = "") }
            }
        }
    }

    private fun startSearchDebounce() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _rawSearchQuery.debounce(200L).collect { query ->
                _debouncedSearchQuery.value = query
            }
        }
    }

    private fun checkSessionRecovery() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val count = entryRepository.getTotalActiveCount()
            if (count > 0) {
                val lastEntry = entryRepository.getLastActiveEntry()
                if (lastEntry != null) {
                    _sessionRecovery.value = SessionRecovery(
                        outletName = lastEntry.outletName,
                        outletCode = lastEntry.outletCode,
                        entryCount = count,
                    )
                }
            }
        }
    }

    private fun loadLockedMerchandiser() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val lock = deviceLockRepository.get()
            if (lock != null) _currentMerchandiser.value = lock.merchandiserName
        }
    }

    private fun loadMasterDataInfo() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val meta = csvMetadataRepository.getByType(CsvMetadataRepository.FILE_TYPE_ITEMS)
            _masterDataInfo.value = MasterDataInfo(
                itemCount   = itemRepository.getCount(),
                outletCount = outletRepository.getCount(),
                importedAt  = meta?.importedAt?.substringBefore(" ") ?: "",
            )
        }
    }

    // ── Session recovery actions ──────────────────────────────────────────────

    fun dismissSessionRecovery() { _sessionRecovery.value = null }

    fun continueSession(recovery: SessionRecovery) {
        _outletName.value      = recovery.outletName
        _outletCode.value      = recovery.outletCode
        _sessionRecovery.value = null
        viewModelScope.launch(Dispatchers.IO + handler) {
            val link = teamLinkRepository.findByOutletCode(recovery.outletCode)
            if (link != null) _currentSalesman.value = link.salesmanName
        }
    }

    // ── Form actions ──────────────────────────────────────────────────────────

    fun setOutletQuery(query: String) {
        _outletName.value  = query
        _outletCode.value  = ""
        _outletQuery.value = query
    }

    fun selectOutlet(outlet: Outlet) {
        _outletName.value    = outlet.outletName
        _outletCode.value    = outlet.outletCode
        _outletQuery.value   = ""
        _outletResults.value = emptyList()
        viewModelScope.launch(Dispatchers.IO + handler) {
            val link = teamLinkRepository.findByOutletCode(outlet.outletCode)
            if (link != null) _currentSalesman.value = link.salesmanName
        }
    }

    fun onNextTapped(): Boolean {
        val ok = _outletName.value.isNotBlank()
        _showFieldErrors.value = !ok
        if (ok) {
            sessionHolder.set(
                merchandiser = _currentMerchandiser.value,
                salesman     = _currentSalesman.value,
                outletName   = _outletName.value,
                outletCode   = _outletCode.value,
            )
        }
        return ok
    }

    // ── Search action ─────────────────────────────────────────────────────────

    fun setSearchQuery(query: String) { _rawSearchQuery.value = query }
}
