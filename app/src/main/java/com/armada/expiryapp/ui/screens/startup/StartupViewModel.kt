package com.armada.expiryapp.ui.screens.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armada.expiryapp.data.auth.AuthRepository
import com.armada.expiryapp.data.repository.CsvMetadataRepository
import com.armada.expiryapp.data.repository.ExpiryEntryRepository
import com.armada.expiryapp.data.repository.ItemRepository
import com.armada.expiryapp.data.repository.OutletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val authRepository:      AuthRepository,
    private val itemRepository:      ItemRepository,
    private val outletRepository:    OutletRepository,
    private val entryRepository:     ExpiryEntryRepository,
    private val csvMetadataRepository: CsvMetadataRepository,
) : ViewModel() {

    data class HealthInfo(
        val itemCount: Int,
        val outletCount: Int,
        val importedAt: String,
    )

    sealed class Route {
        object Loading          : Route()
        object GoToCsvValidation: Route()
        object GoToLogin        : Route()
        data class GoToDashboard(val health: HealthInfo) : Route()
    }

    private val handler = CoroutineExceptionHandler { _, _ ->
        _route.value = Route.GoToCsvValidation
    }

    private val _route = MutableStateFlow<Route>(Route.Loading)
    val route: StateFlow<Route> = _route.asStateFlow()

    private val _healthInfo = MutableStateFlow<HealthInfo?>(null)
    val healthInfo: StateFlow<HealthInfo?> = _healthInfo.asStateFlow()

    init {
        checkStartup()
    }

    private fun checkStartup() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val authenticated = authRepository.isAuthenticated()
            if (!authenticated) {
                val hasCsv = csvMetadataRepository
                    .getByType(CsvMetadataRepository.FILE_TYPE_ITEMS) != null
                _route.value = if (hasCsv) Route.GoToLogin else Route.GoToCsvValidation
            } else {
                routeAuthenticated()
            }
        }
    }

    private suspend fun routeAuthenticated() {
        val itemCount   = itemRepository.getCount()
        val outletCount = outletRepository.getCount()
        if (itemCount == 0 || outletCount == 0) {
            // Data wiped (e.g. fallbackToDestructiveMigration); force re-import
            _route.value = Route.GoToCsvValidation
            return
        }
        val meta = csvMetadataRepository.getByType(CsvMetadataRepository.FILE_TYPE_ITEMS)
        val info = HealthInfo(
            itemCount   = itemCount,
            outletCount = outletCount,
            importedAt  = meta?.importedAt ?: "",
        )
        _healthInfo.value = info

        // Pre-warm LRU barcode cache with last 20 barcodes scanned this session
        val recentEntries = entryRepository.getRecentEntries(20)
        itemRepository.preloadRecentBarcodes(recentEntries)

        _route.value = Route.GoToDashboard(info)
    }

    fun onCsvImportComplete() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            if (authRepository.isAuthenticated()) routeAuthenticated()
            else _route.value = Route.GoToLogin
        }
    }

    fun onLoginSuccess() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            routeAuthenticated()
        }
    }
}
