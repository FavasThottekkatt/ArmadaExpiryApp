package com.armada.expiryapp.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armada.expiryapp.BuildConfig
import com.armada.expiryapp.data.db.entity.CsvMetadata
import com.armada.expiryapp.data.repository.CsvMetadataRepository
import com.armada.expiryapp.data.repository.OutletItemLinkRepository
import com.armada.expiryapp.data.session.SessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ShareRequest(val file: File, val mimeType: String)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val csvMetadataRepository: CsvMetadataRepository,
    private val sessionHolder: SessionHolder,
    private val linkRepository: OutletItemLinkRepository,
) : ViewModel() {

    val csvMetadata: StateFlow<List<CsvMetadata>> = csvMetadataRepository.getAllFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _backupFiles = MutableStateFlow<List<File>>(emptyList())
    val backupFiles: StateFlow<List<File>> = _backupFiles.asStateFlow()

    private val _crashLogContent = MutableStateFlow<String?>(null)
    val crashLogContent: StateFlow<String?> = _crashLogContent.asStateFlow()

    private val _shareRequest = MutableSharedFlow<ShareRequest>(extraBufferCapacity = 1)
    val shareRequest: SharedFlow<ShareRequest> = _shareRequest.asSharedFlow()

    private val _snackMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackMessage: SharedFlow<String> = _snackMessage.asSharedFlow()

    val hasOutlet: Boolean get() = sessionHolder.outletCode.isNotBlank()
    val outletName: String get() = sessionHolder.outletName

    private val _linkedItemCount = MutableStateFlow(0)
    val linkedItemCount: StateFlow<Int> = _linkedItemCount.asStateFlow()

    fun refreshLinkedCount() {
        val code = sessionHolder.outletCode
        viewModelScope.launch(Dispatchers.IO) {
            _linkedItemCount.value = if (code.isBlank()) 0 else linkRepository.getCountForOutlet(code)
        }
    }

    val appVersion: String = BuildConfig.VERSION_NAME
    val versionCode: Int   = BuildConfig.VERSION_CODE

    val crashLogFile: File
        get() = File(context.getExternalFilesDir("logs"), "crash_log.txt")

    init {
        loadBackups()
    }

    fun loadBackups() {
        viewModelScope.launch(Dispatchers.IO) {
            val backupDir = File(context.getExternalFilesDir(null), "Backups")
            val files = backupDir.listFiles { f -> f.extension == "db" }
                ?.sortedByDescending { it.lastModified() }
                ?: emptyList()
            _backupFiles.value = files
        }
    }

    fun loadCrashLog() {
        viewModelScope.launch(Dispatchers.IO) {
            val file = crashLogFile
            _crashLogContent.value = when {
                !file.exists() -> null
                else           -> file.readText().let { text ->
                    if (text.length > 6_000) text.take(6_000) + "\n…[truncated]" else text
                }
            }
        }
    }

    fun dismissCrashLog() {
        _crashLogContent.value = null
    }

    fun shareBackup(file: File) {
        if (!file.exists()) { _snackMessage.tryEmit("Backup file not found."); return }
        _shareRequest.tryEmit(ShareRequest(file, "application/octet-stream"))
    }

    fun shareCrashLog() {
        val file = crashLogFile
        if (!file.exists()) { _snackMessage.tryEmit("No crash log found."); return }
        _shareRequest.tryEmit(ShareRequest(file, "text/plain"))
    }

    fun clearCrashLog() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                crashLogFile.delete()
                _snackMessage.tryEmit("Crash log cleared.")
            } catch (_: Exception) {
                _snackMessage.tryEmit("Could not clear log.")
            }
        }
    }
}
