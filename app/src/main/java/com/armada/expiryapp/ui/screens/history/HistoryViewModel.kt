package com.armada.expiryapp.ui.screens.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.data.db.entity.TeamLink
import com.armada.expiryapp.data.repository.DeviceLockRepository
import com.armada.expiryapp.data.repository.ExpiryEntryRepository
import com.armada.expiryapp.data.repository.TeamLinkRepository
import com.armada.expiryapp.data.session.SessionHolder
import com.armada.expiryapp.util.ExcelExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val sessionHolder: SessionHolder,
    private val entryRepository:      ExpiryEntryRepository,
    private val deviceLockRepository: DeviceLockRepository,
    private val teamLinkRepository:   TeamLinkRepository,
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, t ->
        _snackMessage.tryEmit("Error: ${t.localizedMessage ?: "Unknown"}")
    }

    // ── Linked outlets (loaded once from TeamLink for this device) ────────────

    private val _linkedOutlets = MutableStateFlow<List<TeamLink>>(emptyList())

    // ── Outlet selection ──────────────────────────────────────────────────────

    private val _selectedOutlet = MutableStateFlow<Outlet?>(null)
    val selectedOutlet: StateFlow<Outlet?> = _selectedOutlet.asStateFlow()

    private val _outletQuery = MutableStateFlow("")
    val outletQuery: StateFlow<String> = _outletQuery.asStateFlow()

    private val _outletResults = MutableStateFlow<List<Outlet>>(emptyList())
    val outletResults: StateFlow<List<Outlet>> = _outletResults.asStateFlow()

    // ── Loading flags ─────────────────────────────────────────────────────────

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    // ── Events ────────────────────────────────────────────────────────────────

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    private val _shareFile = MutableSharedFlow<File>(extraBufferCapacity = 1)
    val shareFile: SharedFlow<File> = _shareFile.asSharedFlow()

    // ── Archived count (reactive, auto-updates after archive) ─────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val archivedCount: StateFlow<Int> = _selectedOutlet
        .flatMapLatest { outlet ->
            if (outlet == null) flowOf(0)
            else entryRepository.getArchivedCountFlow(outlet.outletCode)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    // ── Paging archived entries (auto-refreshes when archive runs) ────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val archivedEntries: Flow<PagingData<ExpiryEntry>> = _selectedOutlet
        .flatMapLatest { outlet ->
            if (outlet == null) flowOf(PagingData.empty())
            else Pager(PagingConfig(pageSize = 25, enablePlaceholders = false)) {
                entryRepository.getArchivedEntriesPaged(outlet.outletCode)
            }.flow
        }
        .cachedIn(viewModelScope)

    // ── Init ──────────────────────────────────────────────────────────────────

    init {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val lock = deviceLockRepository.get()
            if (lock != null) {
                val links = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
                _linkedOutlets.value = links
                // Auto-select from active session if outlet is in linked list
                val sessionCode = sessionHolder.outletCode
                if (sessionCode.isNotBlank()) {
                    val link = links.find { it.outletCode == sessionCode }
                    if (link != null) {
                        val outlet = Outlet(outletCode = link.outletCode,
                                            outletName = link.outletName, shortName = "")
                        _selectedOutlet.value = outlet
                        _outletQuery.value    = outlet.outletName
                    }
                }
            }
        }
    }

    // ── Outlet selection ──────────────────────────────────────────────────────

    fun setOutletQuery(query: String) {
        _outletQuery.value = query
        if (_selectedOutlet.value != null) return
        viewModelScope.launch(Dispatchers.IO + handler) {
            val links = _linkedOutlets.value
            _outletResults.value = if (query.isBlank()) {
                links.map { Outlet(outletCode = it.outletCode, outletName = it.outletName, shortName = "") }
            } else {
                links
                    .filter { it.outletName.contains(query, ignoreCase = true) }
                    .map { Outlet(outletCode = it.outletCode, outletName = it.outletName, shortName = "") }
            }
        }
    }

    fun selectOutlet(outlet: Outlet) {
        _selectedOutlet.value = outlet
        _outletQuery.value    = outlet.outletName
        _outletResults.value  = emptyList()
    }

    fun clearOutletSelection() {
        _selectedOutlet.value = null
        _outletQuery.value    = ""
        _outletResults.value  = emptyList()
    }

    // ── Export History ────────────────────────────────────────────────────────

    fun exportHistory() {
        val outlet = _selectedOutlet.value
        if (outlet == null) {
            _snackMessage.tryEmit("Please select an outlet first.")
            return
        }
        _isExporting.value = true
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                val entries = entryRepository.getArchivedEntries(outlet.outletCode)
                if (entries.isEmpty()) {
                    _snackMessage.tryEmit("No archived entries for ${outlet.outletName}.")
                    return@launch
                }
                val monthLabel = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
                val file = ExcelExporter(context).buildAndSaveFile(
                    entries      = entries,
                    outlet       = outlet,
                    merchandiser = sessionHolder.merchandiser,
                    salesman     = sessionHolder.salesman,
                    fileLabel    = "History $monthLabel",
                )
                _shareFile.tryEmit(file)
            } catch (oom: OutOfMemoryError) {
                _snackMessage.tryEmit("Not enough memory to build Excel. Close other apps and retry.")
            } catch (e: Exception) {
                _snackMessage.tryEmit("Export failed: ${e.localizedMessage ?: "Unknown error"}")
            } finally {
                _isExporting.value = false
            }
        }
    }
}
