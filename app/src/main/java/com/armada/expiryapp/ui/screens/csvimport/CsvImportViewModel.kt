package com.armada.expiryapp.ui.screens.csvimport

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armada.expiryapp.data.repository.CsvImportRepository
import com.armada.expiryapp.data.repository.CsvMetadataRepository
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.util.AutoBackup
import com.armada.expiryapp.util.CsvParseResult
import com.armada.expiryapp.util.CsvParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CsvImportViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val csvImportRepository:  CsvImportRepository,
    private val csvMetadataRepository: CsvMetadataRepository,
    private val itemRepository:        ItemRepository,
) : ViewModel() {

    sealed class UiState {
        object CheckingStatus : UiState()
        object Idle : UiState()
        data class ShowValidation(val parseResult: CsvParseResult, val isFirstImport: Boolean) : UiState()
        object Importing : UiState()
        object ImportComplete : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.CheckingStatus)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        checkStatus()
    }

    private fun checkStatus() {
        val handler = CoroutineExceptionHandler { _, t ->
            _uiState.value = UiState.Error("Startup check failed: ${t.localizedMessage}")
        }
        viewModelScope.launch(Dispatchers.IO + handler) {
            val alreadyImported =
                csvMetadataRepository.getByType(CsvMetadataRepository.FILE_TYPE_ITEMS) != null
            if (alreadyImported) {
                _uiState.value = UiState.Idle
            } else {
                parseAssets(isFirstImport = true)
            }
        }
    }

    fun triggerReimport() {
        val handler = CoroutineExceptionHandler { _, t ->
            _uiState.value = UiState.Error("Failed to read CSV files: ${t.localizedMessage}")
        }
        viewModelScope.launch(Dispatchers.IO + handler) {
            parseAssets(isFirstImport = false)
        }
    }

    private suspend fun parseAssets(isFirstImport: Boolean) {
        _uiState.value = UiState.CheckingStatus
        val result = CsvParser.parse(context)
        _uiState.value = UiState.ShowValidation(result, isFirstImport)
    }

    fun confirmImport(parseResult: CsvParseResult) {
        val handler = CoroutineExceptionHandler { _, t ->
            _uiState.value = UiState.Error("Import failed: ${t.localizedMessage}")
        }
        viewModelScope.launch(Dispatchers.IO + handler) {
            _uiState.value = UiState.Importing
            csvImportRepository.importData(parseResult)
            itemRepository.clearCache()
            AutoBackup(context).backup()
            _uiState.value = UiState.ImportComplete
        }
    }
}
