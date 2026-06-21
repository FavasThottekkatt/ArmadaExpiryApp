package com.armada.expiryapp.ui.screens.csvimport

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.CsvParseResult

@Composable
fun CsvValidationScreen(
    uiState:         CsvImportViewModel.UiState,
    onConfirmImport: (CsvParseResult) -> Unit,
    onCancel:        () -> Unit,
) {
    // Block back press on first import — user must complete the import to use the app
    val isFirstImport = uiState is CsvImportViewModel.UiState.ShowValidation &&
            uiState.isFirstImport
    BackHandler(enabled = isFirstImport) { /* swallow — cannot bypass first import */ }

    Box(
        modifier          = Modifier
            .fillMaxSize()
            .background(ArmadaColors.BgApp),
        contentAlignment  = Alignment.Center,
    ) {
        when (uiState) {
            is CsvImportViewModel.UiState.CheckingStatus -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = ArmadaColors.BrandAccent)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text  = "Reading master data…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArmadaColors.TextSecondary,
                    )
                }
            }

            is CsvImportViewModel.UiState.ShowValidation -> {
                ValidationCard(
                    result        = uiState.parseResult,
                    isFirstImport = uiState.isFirstImport,
                    onConfirm     = { onConfirmImport(uiState.parseResult) },
                    onCancel      = onCancel,
                )
            }

            is CsvImportViewModel.UiState.Importing -> {
                // Non-dismissable — blocks UI until import completes
                Dialog(
                    onDismissRequest = {},
                    properties       = DialogProperties(
                        dismissOnBackPress    = false,
                        dismissOnClickOutside = false,
                    ),
                ) {
                    Card(
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
                        elevation = CardDefaults.cardElevation(8.dp),
                    ) {
                        Column(
                            modifier            = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            CircularProgressIndicator(color = ArmadaColors.BrandAccent)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text      = "Importing master data…",
                                style     = MaterialTheme.typography.bodyLarge,
                                color     = ArmadaColors.TextPrimary,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text      = "Please wait. Do not close the app.",
                                style     = MaterialTheme.typography.bodySmall,
                                color     = ArmadaColors.TextSecondary,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }

            is CsvImportViewModel.UiState.Error -> {
                Column(
                    modifier            = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text      = "❌  ${uiState.message}",
                        color     = ArmadaColors.StatusExpired,
                        textAlign = TextAlign.Center,
                        style     = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            else -> {} // Idle / ImportComplete — MainActivity navigates away
        }
    }
}

@Composable
private fun ValidationCard(
    result:        CsvParseResult,
    isFirstImport: Boolean,
    onConfirm:     () -> Unit,
    onCancel:      () -> Unit,
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            Text(
                text       = "📋  CSV Import Preview",
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color      = ArmadaColors.BrandTitle,
            )

            Spacer(Modifier.height(20.dp))

            // ── Items ────────────────────────────────────────────────────
            SectionLabel("ItemList.csv")
            Spacer(Modifier.height(8.dp))
            ValidationRow("✅", "Items found",        result.items.validItems.size,   RowKind.Ok)
            ValidationRow("⚠️", "Duplicate barcodes", result.items.duplicateBarcodes, RowKind.Warn)
            ValidationRow("⚠️", "Blank descriptions", result.items.blankDescriptions, RowKind.Warn)
            ValidationRow("⚠️", "Null product codes", result.items.nullProductCodes,  RowKind.Warn)
            ValidationRow("❌", "Blank barcodes",      result.items.blankBarcodes,    RowKind.Error)

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = ArmadaColors.Border)
            Spacer(Modifier.height(16.dp))

            // ── Outlets ──────────────────────────────────────────────────
            SectionLabel("Outlets.csv")
            Spacer(Modifier.height(8.dp))
            ValidationRow("✅", "Outlets found",    result.outlets.validOutlets.size, RowKind.Ok)
            ValidationRow("⚠️", "Duplicate codes",  result.outlets.duplicateCodes,   RowKind.Warn)
            ValidationRow("❌", "Blank names",       result.outlets.blankNames,       RowKind.Error)

            Spacer(Modifier.height(24.dp))

            // ── Actions ──────────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                if (!isFirstImport) {
                    OutlinedButton(
                        onClick  = onCancel,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(10.dp),
                    ) {
                        Text("Cancel", color = ArmadaColors.TextSecondary)
                    }
                    Spacer(Modifier.width(12.dp))
                }
                Button(
                    onClick  = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = ArmadaColors.BrandAccent),
                ) {
                    Text("Import Data", color = ArmadaColors.TextOnDark)
                }
            }
        }
    }
}

private enum class RowKind { Ok, Warn, Error }

@Composable
private fun SectionLabel(title: String) {
    Text(
        text       = title,
        style      = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color      = ArmadaColors.BrandTitle,
    )
}

@Composable
private fun ValidationRow(icon: String, label: String, value: Int, kind: RowKind) {
    val bg = when (kind) {
        RowKind.Ok    -> ArmadaColors.CsvValidBg
        RowKind.Warn  -> if (value > 0) ArmadaColors.CsvWarnBg   else ArmadaColors.CsvValidBg
        RowKind.Error -> if (value > 0) Color(0xFFFFEBEE)         else ArmadaColors.CsvValidBg
    }
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(bg, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = icon, fontSize = 14.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text     = label,
            modifier = Modifier.weight(1f),
            style    = MaterialTheme.typography.bodyMedium,
            color    = ArmadaColors.TextPrimary,
        )
        Text(
            text       = "%,d".format(value),
            style      = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color      = ArmadaColors.TextPrimary,
        )
    }
}
