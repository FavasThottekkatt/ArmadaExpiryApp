package com.armada.expiryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.DrawerValue;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.NavigationBarItemDefaults;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavHostController;
import com.armada.expiryapp.ui.navigation.BottomNavItem;
import com.armada.expiryapp.ui.navigation.Screen;
import com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel;
import com.armada.expiryapp.ui.screens.login.LoginViewModel;
import com.armada.expiryapp.ui.screens.startup.StartupViewModel;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\u0010\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020%H\u0014J\b\u0010&\u001a\u00020 H\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\t\u001a\u0004\b\f\u0010\rR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\t\u001a\u0004\b\u0011\u0010\u0012R/\u0010\u0016\u001a\u0004\u0018\u00010\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u00158B@BX\u0082\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/armada/expiryapp/MainActivity;", "Landroidx/activity/ComponentActivity;", "<init>", "()V", "startupViewModel", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel;", "getStartupViewModel", "()Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel;", "startupViewModel$delegate", "Lkotlin/Lazy;", "csvImportViewModel", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel;", "getCsvImportViewModel", "()Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel;", "csvImportViewModel$delegate", "loginViewModel", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel;", "getLoginViewModel", "()Lcom/armada/expiryapp/ui/screens/login/LoginViewModel;", "loginViewModel$delegate", "<set-?>", "", "pendingNavigation", "getPendingNavigation", "()Ljava/lang/String;", "setPendingNavigation", "(Ljava/lang/String;)V", "pendingNavigation$delegate", "Landroidx/compose/runtime/MutableState;", "notificationPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "intent", "Landroid/content/Intent;", "requestNotificationPermissionIfNeeded", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy startupViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy csvImportViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy loginViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState pendingNavigation$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> notificationPermissionLauncher = null;
    
    public MainActivity() {
        super(0);
    }
    
    private final com.armada.expiryapp.ui.screens.startup.StartupViewModel getStartupViewModel() {
        return null;
    }
    
    private final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel getCsvImportViewModel() {
        return null;
    }
    
    private final com.armada.expiryapp.ui.screens.login.LoginViewModel getLoginViewModel() {
        return null;
    }
    
    private final java.lang.String getPendingNavigation() {
        return null;
    }
    
    private final void setPendingNavigation(java.lang.String p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onNewIntent(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final void requestNotificationPermissionIfNeeded() {
    }
}