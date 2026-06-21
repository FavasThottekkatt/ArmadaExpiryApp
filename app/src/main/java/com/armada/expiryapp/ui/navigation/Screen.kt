package com.armada.expiryapp.ui.navigation

sealed class Screen(val route: String) {
    data object CsvValidation  : Screen("csv_validation")
    data object Login          : Screen("login")
    data object Dashboard      : Screen("dashboard")
    data object NewEntry       : Screen("new_entry")
    data object BarcodeScanner : Screen("barcode_scanner")
    data object OcrScanner     : Screen("ocr_scanner")
    data object Reports        : Screen("reports")
    data object Stock          : Screen("stock")
    data object History        : Screen("history")
    data object ItemLinking    : Screen("item_linking")
    data object TeamLinking    : Screen("team_linking")
}
