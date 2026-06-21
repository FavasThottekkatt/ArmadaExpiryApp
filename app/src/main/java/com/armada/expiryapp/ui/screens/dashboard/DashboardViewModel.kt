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
import com.armada.expiryapp.data.repository.ExpiryEntryRepository
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.OutletRepository
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
) : ViewModel() {

    companion object {
        val MERCHANDISERS = listOf(
            "AKHIL KAKKARA ANILAN", "AKHIL SUNNY THARAYIL", "AMRIT PARIYAR",
            "BHIMKAJI TAMANG", "BIMAL SUNWAR", "DILIP KUMAR CHHETRI",
            "FIROZKHAN C SULAIMAN", "KRISHNA SHAHI", "NARAYAN GIRI", "SANIL JAGALRAJ",
            "SHUHAIB P", "SUSHIL THING TAMANG", "VISHNU GOPAKUMAR",
            "RAMEES RAJILABEEVI", "MUKESH SHRESHTHA", "OMKARA BABU BANDI",
            "MOHAMMED HASSAN", "ABID ALUNGAL", "KUNJI MOIDEEN", "SARATH MAKKAKOD",
            "SARATH RAJ PR", "MOHAMMED SHAN", "AKBAR SULTAN",
        )
        val SALESMEN = listOf(
            "Muneer", "Rajesh Shrestha", "Noushir", "Sreejith", "Shiva", "Ramraj", "Vishnu Jayalal",
        )
    }

    // ── Stat card counts ──────────────────────────────────────────────────────

    data class DashboardStats(
        val expired:  Int = 0,
        val within30: Int = 0,
        val within60: Int = 0,
        val within90: Int = 0,
    )

    // ── Session recovery ──────────────────────────────────────────────────────

    data class SessionRecovery(
        val outletName: String,
        val outletCode: String,
        val entryCount: Int,
    )

    // ── Master data info strip ────────────────────────────────────────────────

    data class MasterDataInfo(
        val itemCount:   Int,
        val outletCount: Int,
        val importedAt:  String,
    )

    // ── Error handler ─────────────────────────────────────────────────────────

    private val handler = CoroutineExceptionHandler { _, _ -> }

    // ── Form state (Step 1 — Outlet Details) ──────────────────────────────────

    private val _merchandiser = MutableStateFlow("")
    val merchandiser: StateFlow<String> = _merchandiser.asStateFlow()

    private val _salesman = MutableStateFlow("")
    val salesman: StateFlow<String> = _salesman.asStateFlow()

    private val _outletName = MutableStateFlow("")
    val outletName: StateFlow<String> = _outletName.asStateFlow()

    private val _outletCode = MutableStateFlow("")
    val outletCode: StateFlow<String> = _outletCode.asStateFlow()

    private val _outletQuery = MutableStateFlow("")
    private val _outletResults = MutableStateFlow<List<Outlet>>(emptyList())
    val outletResults: StateFlow<List<Outlet>> = _outletResults.asStateFlow()

    private val _showFieldErrors = MutableStateFlow(false)
    val showFieldErrors: StateFlow<Boolean> = _showFieldErrors.asStateFlow()

    val isFormComplete: StateFlow<Boolean> = combine(
        _merchandiser, _salesman, _outletName,
    ) { m, s, o -> m.isNotBlank() && s.isNotBlank() && o.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // ── Session recovery state ────────────────────────────────────────────────

    private val _sessionRecovery = MutableStateFlow<SessionRecovery?>(null)
    val sessionRecovery: StateFlow<SessionRecovery?> = _sessionRecovery.asStateFlow()

    // ── Master data info ──────────────────────────────────────────────────────

    private val _masterDataInfo = MutableStateFlow<MasterDataInfo?>(null)
    val masterDataInfo: StateFlow<MasterDataInfo?> = _masterDataInfo.asStateFlow()

    // ── Dashboard stats (reactive — updates when entries change) ──────────────

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

    // ── Latest records paging ─────────────────────────────────────────────────

    private val _rawSearchQuery      = MutableStateFlow("")
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
    }

    @OptIn(FlowPreview::class)
    private fun startOutletSearch() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _outletQuery.debounce(150L).collect { query ->
                _outletResults.value = if (query.isBlank()) emptyList()
                else outletRepository.searchForDropdown(query)
            }
        }
    }

    @OptIn(FlowPreview::class)
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

    fun dismissSessionRecovery() { _sessionRecovery.value = null }

    fun continueSession(recovery: SessionRecovery) {
        _outletName.value      = recovery.outletName
        _outletCode.value      = recovery.outletCode
        _sessionRecovery.value = null
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

    // ── Form actions ──────────────────────────────────────────────────────────

    fun setMerchandiser(v: String) { _merchandiser.value = v }
    fun setSalesman(v: String)     { _salesman.value     = v }

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
    }

    fun setOutletCode(v: String) { _outletCode.value = v }

    fun onNextTapped(): Boolean {
        val ok = _merchandiser.value.isNotBlank()
                && _salesman.value.isNotBlank()
                && _outletName.value.isNotBlank()
        _showFieldErrors.value = !ok
        if (ok) {
            sessionHolder.set(
                merchandiser = _merchandiser.value,
                salesman     = _salesman.value,
                outletName   = _outletName.value,
                outletCode   = _outletCode.value,
            )
        }
        return ok
    }

    // ── Search action ─────────────────────────────────────────────────────────

    fun setSearchQuery(query: String) { _rawSearchQuery.value = query }
}
