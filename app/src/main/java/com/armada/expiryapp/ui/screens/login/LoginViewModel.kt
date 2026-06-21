package com.armada.expiryapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armada.expiryapp.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    sealed class UiState {
        object Idle         : UiState()
        object Verifying    : UiState()
        object LoginSuccess : UiState()
        object LoginError   : UiState()
    }

    private val handler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = UiState.LoginError
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun submitPassword(input: String) {
        if (_uiState.value == UiState.Verifying) return
        _uiState.value = UiState.Verifying
        viewModelScope.launch(Dispatchers.IO + handler) {
            val ok = authRepository.verifyPassword(input)
            if (ok) {
                authRepository.setAuthenticated()
                _uiState.value = UiState.LoginSuccess
            } else {
                _uiState.value = UiState.LoginError
            }
        }
    }

    fun resetError() {
        if (_uiState.value == UiState.LoginError) _uiState.value = UiState.Idle
    }
}
