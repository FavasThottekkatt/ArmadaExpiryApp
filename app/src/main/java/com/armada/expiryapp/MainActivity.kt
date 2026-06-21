package com.armada.expiryapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.armada.expiryapp.ui.navigation.AppNavGraph
import com.armada.expiryapp.ui.navigation.BottomNavItem
import com.armada.expiryapp.ui.navigation.Screen
import com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel
import com.armada.expiryapp.ui.screens.login.LoginViewModel
import com.armada.expiryapp.ui.screens.settings.SettingsDrawerContent
import com.armada.expiryapp.ui.screens.startup.StartupViewModel
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.ui.theme.ArmadaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val startupViewModel:   StartupViewModel   by viewModels()
    private val csvImportViewModel: CsvImportViewModel by viewModels()
    private val loginViewModel:     LoginViewModel     by viewModels()

    // Reactive: triggers recomposition so ArmadaApp can navigate when the value changes.
    private var pendingNavigation: String? by mutableStateOf(null)

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Capture notification-tap navigation target
        pendingNavigation = intent?.getStringExtra("navigate_to")

        requestNotificationPermissionIfNeeded()

        setContent {
            ArmadaTheme {
                ArmadaApp(
                    startupViewModel   = startupViewModel,
                    csvImportViewModel = csvImportViewModel,
                    loginViewModel     = loginViewModel,
                    pendingNavigation  = pendingNavigation,
                    onNavHandled       = { pendingNavigation = null },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingNavigation = intent.getStringExtra("navigate_to")
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArmadaApp(
    startupViewModel:   StartupViewModel,
    csvImportViewModel: CsvImportViewModel,
    loginViewModel:     LoginViewModel,
    pendingNavigation:  String?,
    onNavHandled:       () -> Unit,
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    val startupRoute by startupViewModel.route.collectAsState()
    val csvState     by csvImportViewModel.uiState.collectAsState()
    val loginState   by loginViewModel.uiState.collectAsState()

    LaunchedEffect(csvState) {
        if (csvState is CsvImportViewModel.UiState.ImportComplete) {
            startupViewModel.onCsvImportComplete()
        }
    }

    LaunchedEffect(loginState) {
        if (loginState is LoginViewModel.UiState.LoginSuccess) {
            startupViewModel.onLoginSuccess()
        }
    }

    LaunchedEffect(startupRoute) {
        val cur = navController.currentBackStackEntry?.destination?.route
        when (startupRoute) {
            is StartupViewModel.Route.GoToCsvValidation -> {
                if (cur != Screen.CsvValidation.route) {
                    navController.navigate(Screen.CsvValidation.route) {
                        launchSingleTop = true
                    }
                }
            }
            is StartupViewModel.Route.GoToLogin -> {
                if (cur != Screen.Login.route) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.CsvValidation.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is StartupViewModel.Route.GoToDashboard -> {
                if (cur != Screen.Dashboard.route) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            StartupViewModel.Route.Loading -> { /* wait */ }
        }
    }

    // Navigate to Reports when the app is opened from a notification tap.
    LaunchedEffect(pendingNavigation, startupRoute) {
        if (startupRoute is StartupViewModel.Route.GoToDashboard &&
            pendingNavigation == "reports"
        ) {
            navController.navigate(Screen.Reports.route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState    = true
            }
            onNavHandled()
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Dashboard, "Dashboard", Icons.Filled.Home),
        BottomNavItem(Screen.NewEntry,  "New Entry",  Icons.Filled.AddCircle),
        BottomNavItem(Screen.Reports,   "Reports",    Icons.Filled.Assessment),
        BottomNavItem(Screen.Stock,     "Stock",      Icons.Filled.Inventory2),
        BottomNavItem(Screen.History,   "History",    Icons.Filled.History),
    )

    val showShell = currentRoute != Screen.CsvValidation.route
                 && currentRoute != Screen.Login.route

    ModalNavigationDrawer(
        drawerState     = drawerState,
        gesturesEnabled = showShell,
        drawerContent   = {
            ModalDrawerSheet {
                SettingsDrawerContent(
                    onReimport = {
                        csvImportViewModel.triggerReimport()
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.CsvValidation.route) {
                            launchSingleTop = true
                        }
                    },
                    onItemLinking = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.ItemLinking.route) {
                            launchSingleTop = true
                        }
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                if (showShell) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector        = Icons.Filled.Menu,
                                    contentDescription = "Open settings",
                                    tint               = ArmadaColors.TextOnDark,
                                )
                            }
                        },
                        title = {
                            Text(
                                text       = "Armada Distribution AUH & AL AIN",
                                color      = ArmadaColors.TextOnDark,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 18.sp,
                                maxLines   = 1,
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = ArmadaColors.BgHeader,
                        ),
                    )
                }
            },
            bottomBar = {
                if (showShell) {
                    NavigationBar(containerColor = ArmadaColors.BgCard) {
                        bottomNavItems.forEach { item ->
                            val selected = currentRoute == item.screen.route
                            NavigationBarItem(
                                selected = selected,
                                onClick  = {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState    = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector        = item.icon,
                                        contentDescription = item.label,
                                        tint = if (selected) ArmadaColors.NavActive else ArmadaColors.NavInactive,
                                    )
                                },
                                label = {
                                    Text(
                                        text  = item.label,
                                        color = if (selected) ArmadaColors.NavActive else ArmadaColors.NavInactive,
                                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = ArmadaColors.BrandAccent.copy(alpha = 0.15f),
                                ),
                            )
                        }
                    }
                }
            },
            containerColor      = ArmadaColors.BgApp,
            contentWindowInsets = WindowInsets(0.dp),
            modifier            = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavGraph(
                    navController      = navController,
                    csvImportViewModel = csvImportViewModel,
                    loginViewModel     = loginViewModel,
                    modifier           = Modifier.fillMaxSize(),
                )

                if (startupRoute == StartupViewModel.Route.Loading) {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = ArmadaColors.BrandAccent)
                    }
                }
            }
        }
    }
}
