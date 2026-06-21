package com.armada.expiryapp.ui.screens.teamlinking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armada.expiryapp.data.db.entity.DeviceLock
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.data.db.entity.TeamLink
import com.armada.expiryapp.data.repository.DeviceLockRepository
import com.armada.expiryapp.data.repository.OutletRepository
import com.armada.expiryapp.data.repository.TeamLinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TeamLinkingViewModel @Inject constructor(
    private val deviceLockRepository: DeviceLockRepository,
    private val teamLinkRepository:   TeamLinkRepository,
    private val outletRepository:     OutletRepository,
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
        private val TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        _snackMessage.tryEmit("Error: ${t.localizedMessage ?: "Unknown"}")
    }

    // ── DeviceLock (reactive) ─────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val deviceLock: StateFlow<DeviceLock?> = deviceLockRepository.getFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // ── TeamLinks (reactive — updates immediately after insert/delete) ─────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val teamLinks: StateFlow<List<TeamLink>> = deviceLock
        .flatMapLatest { lock ->
            if (lock == null) flowOf(emptyList())
            else teamLinkRepository.getAllForMerchandiserFlow(lock.merchandiserName)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ── Form inputs ───────────────────────────────────────────────────────────

    private val _merchandiserInput    = MutableStateFlow("")
    val merchandiserInput: StateFlow<String> = _merchandiserInput.asStateFlow()

    private val _salesmanInput        = MutableStateFlow("")
    val salesmanInput: StateFlow<String> = _salesmanInput.asStateFlow()

    private val _outletQuery          = MutableStateFlow("")
    val outletQuery: StateFlow<String> = _outletQuery.asStateFlow()

    private val _selectedOutletCode   = MutableStateFlow("")
    val selectedOutletCode: StateFlow<String> = _selectedOutletCode.asStateFlow()

    private val _selectedOutletName   = MutableStateFlow("")

    private val _outletResults        = MutableStateFlow<List<Outlet>>(emptyList())
    val outletResults: StateFlow<List<Outlet>> = _outletResults.asStateFlow()

    // ── Derived state ─────────────────────────────────────────────────────────

    val isLinkReady: StateFlow<Boolean> = combine(
        deviceLock, _merchandiserInput, _salesmanInput, _selectedOutletCode,
    ) { lock, merch, salesman, outletCode ->
        val hasMerch = lock != null || merch.isNotBlank()
        hasMerch && salesman.isNotBlank() && outletCode.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isDoneEnabled: StateFlow<Boolean> = teamLinks
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // ── Dialog states ─────────────────────────────────────────────────────────

    private val _showDoneConfirm    = MutableStateFlow(false)
    val showDoneConfirm: StateFlow<Boolean> = _showDoneConfirm.asStateFlow()

    private val _deleteConfirmLink  = MutableStateFlow<TeamLink?>(null)
    val deleteConfirmLink: StateFlow<TeamLink?> = _deleteConfirmLink.asStateFlow()

    private val _showBackWarning    = MutableStateFlow(false)
    val showBackWarning: StateFlow<Boolean> = _showBackWarning.asStateFlow()

    // ── One-shot navigation event ─────────────────────────────────────────────

    private val _navigateBack = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateBack: SharedFlow<Unit> = _navigateBack.asSharedFlow()

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    // ── Init ──────────────────────────────────────────────────────────────────

    init {
        startOutletSearch()
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

    // ── Form actions ──────────────────────────────────────────────────────────

    fun setMerchandiserInput(v: String) { _merchandiserInput.value = v }
    fun setSalesmanInput(v: String)     { _salesmanInput.value = v }

    fun setOutletQuery(query: String) {
        _outletQuery.value        = query
        _selectedOutletCode.value = ""
        _selectedOutletName.value = ""
    }

    fun selectOutlet(outlet: Outlet) {
        _outletQuery.value        = outlet.outletName
        _selectedOutletCode.value = outlet.outletCode
        _selectedOutletName.value = outlet.outletName
        _outletResults.value      = emptyList()
    }

    // ── LINK button ───────────────────────────────────────────────────────────

    fun tapLink() {
        val lock        = deviceLock.value
        val merchName   = lock?.merchandiserName ?: _merchandiserInput.value.trim()
        val salesName   = _salesmanInput.value.trim()
        val outletCode  = _selectedOutletCode.value
        val outletName  = _selectedOutletName.value

        if (merchName.isBlank() || salesName.isBlank() || outletCode.isBlank()) {
            _snackMessage.tryEmit("Please fill all required fields.")
            return
        }

        viewModelScope.launch(Dispatchers.IO + handler) {
            // First link ever — save DeviceLock
            if (lock == null) {
                deviceLockRepository.upsert(
                    DeviceLock(
                        merchandiserName = merchName,
                        lockedAt = LocalDateTime.now().format(TIMESTAMP_FMT),
                    )
                )
            }

            // Check duplicate
            if (teamLinkRepository.isOutletLinked(outletCode, merchName)) {
                _snackMessage.tryEmit("This outlet is already linked. Delete it first to relink.")
                return@launch
            }

            // Save link
            teamLinkRepository.insert(
                TeamLink(
                    merchandiserName = merchName,
                    salesmanName     = salesName,
                    outletCode       = outletCode,
                    outletName       = outletName,
                )
            )

            // Clear salesman + outlet fields (keep merchandiser)
            _salesmanInput.value      = ""
            _outletQuery.value        = ""
            _selectedOutletCode.value = ""
            _selectedOutletName.value = ""
            _outletResults.value      = emptyList()
        }
    }

    // ── Delete link ───────────────────────────────────────────────────────────

    fun requestDeleteLink(link: TeamLink) { _deleteConfirmLink.value = link }
    fun dismissDeleteLink()               { _deleteConfirmLink.value = null }
    fun confirmDeleteLink() {
        val link = _deleteConfirmLink.value ?: return
        _deleteConfirmLink.value = null
        viewModelScope.launch(Dispatchers.IO + handler) {
            teamLinkRepository.deleteById(link.id)
        }
    }

    // ── DONE button ───────────────────────────────────────────────────────────

    fun tapDone()      { if (isDoneEnabled.value) _showDoneConfirm.value = true }
    fun dismissDone()  { _showDoneConfirm.value = false }
    fun confirmDone()  {
        _showDoneConfirm.value = false
        _navigateBack.tryEmit(Unit)
    }

    // ── Back press ────────────────────────────────────────────────────────────

    fun requestBackWarning() { _showBackWarning.value = true }
    fun dismissBackWarning() { _showBackWarning.value = false }
}
