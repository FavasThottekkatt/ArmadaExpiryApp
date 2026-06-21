package com.armada.expiryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel
import com.armada.expiryapp.ui.screens.csvimport.CsvValidationScreen
import com.armada.expiryapp.ui.screens.dashboard.DashboardScreen
import com.armada.expiryapp.ui.screens.entry.NewEntryScreen
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel
import com.armada.expiryapp.ui.screens.history.HistoryScreen
import com.armada.expiryapp.ui.screens.history.HistoryViewModel
import com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingScreen
import com.armada.expiryapp.ui.screens.login.LoginScreen
import com.armada.expiryapp.ui.screens.login.LoginViewModel
import com.armada.expiryapp.ui.screens.reports.ReportsScreen
import com.armada.expiryapp.ui.screens.stock.StockScreen
import com.armada.expiryapp.ui.screens.ocr.OcrScannerScreen
import com.armada.expiryapp.ui.screens.scanner.BarcodeScannerScreen
import com.armada.expiryapp.util.ScanFeedback

@Composable
fun AppNavGraph(
    navController:      NavHostController,
    csvImportViewModel: CsvImportViewModel,
    loginViewModel:     LoginViewModel,
    modifier:           Modifier = Modifier,
) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Dashboard.route,
        modifier         = modifier,
    ) {
        composable(Screen.CsvValidation.route) {
            val uiState by csvImportViewModel.uiState.collectAsState()
            CsvValidationScreen(
                uiState         = uiState,
                onConfirmImport = csvImportViewModel::confirmImport,
                onCancel        = { navController.popBackStack() },
            )
        }

        composable(Screen.Login.route) {
            val uiState by loginViewModel.uiState.collectAsState()
            LoginScreen(
                uiState          = uiState,
                onPasswordSubmit = loginViewModel::submitPassword,
                onErrorHandled   = loginViewModel::resetError,
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNextClick        = { navController.navigate(Screen.NewEntry.route) },
                onViewArchiveClick = { navController.navigate(Screen.History.route) },
            )
        }

        composable(Screen.NewEntry.route) { backStackEntry ->
            val viewModel: NewEntryViewModel = hiltViewModel()

            // Receive barcode result from the barcode scanner
            val barcodeResult by backStackEntry.savedStateHandle
                .getStateFlow<String?>("barcode_result", null)
                .collectAsState()

            LaunchedEffect(barcodeResult) {
                barcodeResult?.let { barcode ->
                    viewModel.onBarcodeScanned(barcode)
                    backStackEntry.savedStateHandle.remove<String>("barcode_result")
                }
            }

            // Receive OCR date result from the OCR scanner
            val ocrDateResult by backStackEntry.savedStateHandle
                .getStateFlow<String?>("ocr_date_result", null)
                .collectAsState()

            LaunchedEffect(ocrDateResult) {
                ocrDateResult?.let { raw ->
                    viewModel.onOcrDateScanned(raw)
                    backStackEntry.savedStateHandle.remove<String>("ocr_date_result")
                }
            }

            NewEntryScreen(
                viewModel   = viewModel,
                onScanClick = { navController.navigate(Screen.BarcodeScanner.route) },
                onOcrClick  = { navController.navigate(Screen.OcrScanner.route) },
            )
        }

        composable(Screen.BarcodeScanner.route) {
            val view = LocalView.current
            BarcodeScannerScreen(
                onBarcodeDetected = { barcode ->
                    ScanFeedback.play(view)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("barcode_result", barcode)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.OcrScanner.route) {
            OcrScannerScreen(
                onDateDetected  = { rawDigits ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("ocr_date_result", rawDigits)
                    navController.popBackStack()
                },
                onEnterManually = { navController.popBackStack() },
                onBack          = { navController.popBackStack() },
            )
        }

        composable(Screen.Reports.route) {
            val viewModel: com.armada.expiryapp.ui.screens.reports.ReportsViewModel = hiltViewModel()
            ReportsScreen(viewModel = viewModel)
        }

        composable(Screen.Stock.route) {
            val viewModel: com.armada.expiryapp.ui.screens.stock.StockViewModel = hiltViewModel()
            StockScreen(viewModel = viewModel)
        }

        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(viewModel = viewModel)
        }

        composable(Screen.ItemLinking.route) {
            ItemLinkingScreen(onBack = { navController.popBackStack() })
        }
    }
}
