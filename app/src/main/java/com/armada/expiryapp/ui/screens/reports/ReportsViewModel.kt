package com.armada.expiryapp.ui.screens.reports

import android.content.Context
import android.os.Environment
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
import com.armada.expiryapp.util.AutoBackup
import com.armada.expiryapp.util.ExcelExporter
import com.armada.expiryapp.util.toDisplayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val sessionHolder: SessionHolder,
    private val entryRepository:      ExpiryEntryRepository,
    private val deviceLockRepository: DeviceLockRepository,
    private val teamLinkRepository:   TeamLinkRepository,
) : ViewModel() {

    data class SummaryData(
        val outletName:    String,
        val itemCount:     Int,
        val expiredCount:  Int,
        val within30Count: Int,
        val merchandiser:  String,
        val salesman:      String,
        val monthLabel:    String,
    )

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

    // ── Summary ───────────────────────────────────────────────────────────────

    private val _summaryData = MutableStateFlow<SummaryData?>(null)
    val summaryData: StateFlow<SummaryData?> = _summaryData.asStateFlow()

    // ── Export ────────────────────────────────────────────────────────────────

    private val _isExportingExcel = MutableStateFlow(false)
    val isExportingExcel: StateFlow<Boolean> = _isExportingExcel.asStateFlow()

    private val _shareFile = MutableSharedFlow<File>(extraBufferCapacity = 1)
    val shareFile: SharedFlow<File> = _shareFile.asSharedFlow()

    private val _shareText = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val shareText: SharedFlow<String> = _shareText.asSharedFlow()

    private val _showTextScopeDialog = MutableStateFlow(false)
    val showTextScopeDialog: StateFlow<Boolean> = _showTextScopeDialog.asStateFlow()

    // ── Paging entries ────────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val reportEntries: Flow<PagingData<ExpiryEntry>> = _selectedOutlet
        .flatMapLatest { outlet ->
            if (outlet == null) flowOf(PagingData.empty())
            else Pager(PagingConfig(pageSize = 25, enablePlaceholders = false)) {
                entryRepository.getActiveEntriesPaged(outlet.outletCode)
            }.flow
        }
        .cachedIn(viewModelScope)

    // ── Snackbar ──────────────────────────────────────────────────────────────

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    // ── Past exports ──────────────────────────────────────────────────────────

    private val _pastExports = MutableStateFlow<List<File>>(emptyList())
    val pastExports: StateFlow<List<File>> = _pastExports.asStateFlow()

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
                        loadSummary(outlet)
                    }
                }
            }
        }
        loadPastExports()
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
        viewModelScope.launch(Dispatchers.IO + handler) { loadSummary(outlet) }
    }

    fun clearOutletSelection() {
        _selectedOutlet.value = null
        _outletQuery.value    = ""
        _outletResults.value  = emptyList()
        _summaryData.value    = null
    }

    private suspend fun loadSummary(outlet: Outlet) {
        val today  = LocalDate.now()
        val todayStr = today.toString()
        val d30Str   = today.plusDays(30).toString()
        val items    = entryRepository.getActiveCountForOutlet(outlet.outletCode)
        val expired  = entryRepository.getExpiredCount(outlet.outletCode, todayStr)
        val within30 = entryRepository.getWithin30Count(outlet.outletCode, todayStr, d30Str)
        val monthLabel = today.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
        _summaryData.value = SummaryData(
            outletName    = outlet.outletName,
            itemCount     = items,
            expiredCount  = expired,
            within30Count = within30,
            merchandiser  = sessionHolder.merchandiser,
            salesman      = sessionHolder.salesman,
            monthLabel    = monthLabel,
        )
    }

    // ── Excel export ──────────────────────────────────────────────────────────

    fun exportExcel() {
        _isExportingExcel.value = true
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                val lock = deviceLockRepository.get() ?: run {
                    _snackMessage.tryEmit("Merchandiser not set. Complete Team Linking first.")
                    _isExportingExcel.value = false
                    return@launch
                }
                val links = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
                if (links.isEmpty()) {
                    _snackMessage.tryEmit("No outlets linked.")
                    _isExportingExcel.value = false
                    return@launch
                }
                val monthPrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
                val outletEntries = mutableListOf<Pair<Outlet, List<ExpiryEntry>>>()
                links.forEach { link ->
                    val entries = entryRepository.getEntriesForExport(
                        outletCode   = link.outletCode,
                        merchandiser = lock.merchandiserName,
                        salesman     = link.salesmanName,
                        monthPrefix  = monthPrefix,
                    )
                    outletEntries.add(
                        Outlet(outletCode = link.outletCode, outletName = link.outletName,
                               shortName  = link.outletName.take(31)) to entries
                    )
                }
                val file = ExcelExporter(context).buildMultiOutletFile(
                    outletEntries = outletEntries,
                    merchandiser  = lock.merchandiserName,
                )
                triggerAutoBackup()
                loadPastExports()
                _shareFile.tryEmit(file)
            } catch (oom: OutOfMemoryError) {
                _snackMessage.tryEmit("Not enough memory to build Excel. Close other apps and retry.")
            } catch (e: Exception) {
                _snackMessage.tryEmit("Export failed: ${e.localizedMessage ?: "Unknown error"}")
            } finally {
                _isExportingExcel.value = false
            }
        }
    }

    // ── Text report ───────────────────────────────────────────────────────────

    fun requestTextReport() {
        if (_selectedOutlet.value == null) {
            _snackMessage.tryEmit("Please select an outlet first.")
            return
        }
        _showTextScopeDialog.value = true
    }

    fun dismissTextScopeDialog() { _showTextScopeDialog.value = false }

    fun shareTextThisOutlet() {
        _showTextScopeDialog.value = false
        val outlet = _selectedOutlet.value ?: return
        viewModelScope.launch(Dispatchers.IO + handler) {
            val monthPrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
            val entries = entryRepository.getEntriesForExport(
                outlet.outletCode, sessionHolder.merchandiser, sessionHolder.salesman, monthPrefix,
            )
            _shareText.tryEmit(buildTextReport(listOf(outlet.outletName to entries)))
        }
    }

    fun shareTextAllOutlets() {
        _showTextScopeDialog.value = false
        viewModelScope.launch(Dispatchers.IO + handler) {
            val lock = deviceLockRepository.get() ?: run {
                _snackMessage.tryEmit("Merchandiser not set. Complete Team Linking first.")
                return@launch
            }
            val links = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
            if (links.isEmpty()) {
                _snackMessage.tryEmit("No outlets linked. Complete Team Linking first.")
                return@launch
            }
            val monthPrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
            val sections = mutableListOf<Pair<String, List<ExpiryEntry>>>()
            links.forEach { link ->
                val entries = entryRepository.getEntriesForExport(
                    outletCode   = link.outletCode,
                    merchandiser = lock.merchandiserName,
                    salesman     = link.salesmanName,
                    monthPrefix  = monthPrefix,
                )
                if (entries.isNotEmpty()) sections.add(link.outletName to entries)
            }
            if (sections.isEmpty()) {
                _snackMessage.tryEmit("No entries found for any linked outlet this month.")
                return@launch
            }
            _shareText.tryEmit(buildTextReport(sections))
        }
    }

    private fun buildTextReport(sections: List<Pair<String, List<ExpiryEntry>>>): String {
        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMMM yyyy  hh:mm a", Locale.getDefault()))
        val sb = StringBuilder("*Expiry Report*\n$dateTime\n")
        sections.forEach { (outletName, entries) ->
            sb.append("\n")
            sb.append("*$outletName*\n\n")
            if (entries.isEmpty()) {
                sb.append("  (no entries this month)\n")
            } else {
                entries.forEach { e ->
                    sb.append("${e.description} - ${e.expiryDate.toDisplayDate()} - ${e.quantity} ${e.unit}\n")
                }
            }
        }
        return sb.toString().trimEnd()
    }

    // ── Auto-backup ───────────────────────────────────────────────────────────

    private fun triggerAutoBackup() {
        viewModelScope.launch(Dispatchers.IO) { AutoBackup(context).backup() }
    }

    // ── Past exports ──────────────────────────────────────────────────────────

    fun emitSnack(msg: String) { _snackMessage.tryEmit(msg) }

    fun loadPastExports() {
        viewModelScope.launch(Dispatchers.IO) {
            val docsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val files = docsDir
                ?.listFiles { f -> f.extension.equals("xlsx", ignoreCase = true) }
                ?.sortedByDescending { it.lastModified() }
                ?: emptyList()
            _pastExports.value = files
        }
    }
}
