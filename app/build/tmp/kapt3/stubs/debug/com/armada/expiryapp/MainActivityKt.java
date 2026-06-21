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

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a8\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u00a8\u0006\f"}, d2 = {"ArmadaApp", "", "startupViewModel", "Lcom/armada/expiryapp/ui/screens/startup/StartupViewModel;", "csvImportViewModel", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel;", "loginViewModel", "Lcom/armada/expiryapp/ui/screens/login/LoginViewModel;", "pendingNavigation", "", "onNavHandled", "Lkotlin/Function0;", "app_debug"})
public final class MainActivityKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void ArmadaApp(com.armada.expiryapp.ui.screens.startup.StartupViewModel startupViewModel, com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel csvImportViewModel, com.armada.expiryapp.ui.screens.login.LoginViewModel loginViewModel, java.lang.String pendingNavigation, kotlin.jvm.functions.Function0<kotlin.Unit> onNavHandled) {
    }
}