package com.armada.expiryapp.ui.screens.login;

import androidx.lifecycle.ViewModel;
import com.armada.expiryapp.data.auth.AuthRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0014B\u0011\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0015"}, d2 = {"Lcom/armada/expiryapp/ui/screens/login/LoginViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/armada/expiryapp/data/auth/AuthRepository;", "<init>", "(Lcom/armada/expiryapp/data/auth/AuthRepository;)V", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "submitPassword", "", "input", "", "resetError", "UiState", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LoginViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.auth.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState> uiState = null;
    
    @javax.inject.Inject()
    public LoginViewModel(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.auth.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState> getUiState() {
        return null;
    }
    
    public final void submitPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
    }
    
    public final void resetError() {
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0004\u0005\u0006\u0007B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0004\b\t\n\u000b\u00a8\u0006\f"}, d2 = {"Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState;", "", "<init>", "()V", "Idle", "Verifying", "LoginSuccess", "LoginError", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$Idle;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$LoginError;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$LoginSuccess;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$Verifying;", "app_debug"})
    public static abstract class UiState {
        
        private UiState() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$Idle;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class Idle extends com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState.Idle INSTANCE = null;
            
            private Idle() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$LoginError;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class LoginError extends com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState.LoginError INSTANCE = null;
            
            private LoginError() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$LoginSuccess;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class LoginSuccess extends com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState.LoginSuccess INSTANCE = null;
            
            private LoginSuccess() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState$Verifying;", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class Verifying extends com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.login.LoginViewModel.UiState.Verifying INSTANCE = null;
            
            private Verifying() {
            }
        }
    }
}