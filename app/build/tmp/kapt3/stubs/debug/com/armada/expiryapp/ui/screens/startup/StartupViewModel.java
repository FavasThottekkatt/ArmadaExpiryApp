package com.armada.expiryapp.ui.screens.startup;

import androidx.lifecycle.ViewModel;
import com.armada.expiryapp.data.auth.AuthRepository;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001:\u0002!\"B1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0004\b\f\u0010\rJ\b\u0010\u001b\u001a\u00020\u001cH\u0002J\u000e\u0010\u001d\u001a\u00020\u001cH\u0082@\u00a2\u0006\u0002\u0010\u001eJ\u0006\u0010\u001f\u001a\u00020\u001cJ\u0006\u0010 \u001a\u00020\u001cR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00120\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016\u00a8\u0006#"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/armada/expiryapp/data/auth/AuthRepository;", "itemRepository", "Lcom/armada/expiryapp/data/repository/ItemRepository;", "outletRepository", "Lcom/armada/expiryapp/data/repository/OutletRepository;", "entryRepository", "Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;", "csvMetadataRepository", "Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;", "<init>", "(Lcom/armada/expiryapp/data/auth/AuthRepository;Lcom/armada/expiryapp/data/repository/ItemRepository;Lcom/armada/expiryapp/data/repository/OutletRepository;Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;)V", "handler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "_route", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route;", "route", "Lkotlinx/coroutines/flow/StateFlow;", "getRoute", "()Lkotlinx/coroutines/flow/StateFlow;", "_healthInfo", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$HealthInfo;", "healthInfo", "getHealthInfo", "checkStartup", "", "routeAuthenticated", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onCsvImportComplete", "onLoginSuccess", "HealthInfo", "Route", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StartupViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.auth.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ItemRepository itemRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.OutletRepository outletRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route> _route = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route> route = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo> _healthInfo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo> healthInfo = null;
    
    @javax.inject.Inject()
    public StartupViewModel(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.auth.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ItemRepository itemRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.OutletRepository outletRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ExpiryEntryRepository entryRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route> getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo> getHealthInfo() {
        return null;
    }
    
    private final void checkStartup() {
    }
    
    private final java.lang.Object routeAuthenticated(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void onCsvImportComplete() {
    }
    
    public final void onLoginSuccess() {
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0006H\u00c6\u0003J\'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$HealthInfo;", "", "itemCount", "", "outletCount", "importedAt", "", "<init>", "(IILjava/lang/String;)V", "getItemCount", "()I", "getOutletCount", "getImportedAt", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    public static final class HealthInfo {
        private final int itemCount = 0;
        private final int outletCount = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String importedAt = null;
        
        public HealthInfo(int itemCount, int outletCount, @org.jetbrains.annotations.NotNull()
        java.lang.String importedAt) {
            super();
        }
        
        public final int getItemCount() {
            return 0;
        }
        
        public final int getOutletCount() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getImportedAt() {
            return null;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo copy(int itemCount, int outletCount, @org.jetbrains.annotations.NotNull()
        java.lang.String importedAt) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0004\u0005\u0006\u0007B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0004\b\t\n\u000b\u00a8\u0006\f"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route;", "", "<init>", "()V", "Loading", "GoToCsvValidation", "GoToLogin", "GoToDashboard", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$GoToCsvValidation;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$GoToDashboard;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$GoToLogin;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$Loading;", "app_debug"})
    public static abstract class Route {
        
        private Route() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$GoToCsvValidation;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route;", "<init>", "()V", "app_debug"})
        public static final class GoToCsvValidation extends com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route.GoToCsvValidation INSTANCE = null;
            
            private GoToCsvValidation() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$GoToDashboard;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route;", "health", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$HealthInfo;", "<init>", "(Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$HealthInfo;)V", "getHealth", "()Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$HealthInfo;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class GoToDashboard extends com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route {
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo health = null;
            
            public GoToDashboard(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo health) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo getHealth() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route.GoToDashboard copy(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.ui.screens.startup.StartupViewModel.HealthInfo health) {
                return null;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$GoToLogin;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route;", "<init>", "()V", "app_debug"})
        public static final class GoToLogin extends com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route.GoToLogin INSTANCE = null;
            
            private GoToLogin() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route$Loading;", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel$Route;", "<init>", "()V", "app_debug"})
        public static final class Loading extends com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.startup.StartupViewModel.Route.Loading INSTANCE = null;
            
            private Loading() {
            }
        }
    }
}