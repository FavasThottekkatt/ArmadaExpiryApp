@file:OptIn(ExperimentalMaterial3Api::class)

package com.armada.expiryapp.ui.screens.reports

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.toDisplayDate
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel(),
) {
    val context           = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedOutlet   by viewModel.selectedOutlet.collectAsState()
    val summaryData      by viewModel.summaryData.collectAsState()
    val isExportingExcel by viewModel.isExportingExcel.collectAsState()
    val pastExports      by viewModel.pastExports.collectAsState()
    val reportEntries    = viewModel.reportEntries.collectAsLazyPagingItems()

    // Observe one-shot share events
    LaunchedEffect(Unit) {
        viewModel.snackMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.shareFile.collect { file ->
            try {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file,
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Share Excel Report"))
            } catch (_: ActivityNotFoundException) {
                snackbarHostState.showSnackbar("No app found to share this file.")
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Share failed: ${e.localizedMessage ?: "error"}")
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.shareText.collect { text ->
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                context.startActivity(Intent.createChooser(intent, "Share Text Report"))
            } catch (_: ActivityNotFoundException) {
                snackbarHostState.showSnackbar("No app found to share text.")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(ArmadaColors.BgApp)) {

        LazyColumn(
            modifier          = Modifier.fillMaxSize(),
            contentPadding    = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            // ── Outlet label (auto-selected from session) ─────────────────────
            item {
                if (selectedOutlet != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(containerColor = ArmadaColors.BgHeader),
                        shape    = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text       = selectedOutlet!!.outletName,
                            color      = ArmadaColors.TextOnDark,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            modifier   = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        )
                    }
                } else {
                    Text(
                        text     = "Please select an outlet from Dashboard first.",
                        color    = ArmadaColors.TextSecondary,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            }

            // ── Summary card ──────────────────────────────────────────────────
            summaryData?.let { s ->
                item {
                    SummaryCard(s)
                }

                // ── Share buttons ─────────────────────────────────────────────
                item {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick  = viewModel::exportExcel,
                            enabled  = !isExportingExcel,
                            modifier = Modifier.weight(1f),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = ArmadaColors.BrandAccent,
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            if (isExportingExcel) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color    = Color.White,
                                    strokeWidth = 2.dp,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Building…", color = Color.White)
                            } else {
                                Icon(
                                    Icons.Filled.Description,
                                    contentDescription = null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Share Excel", color = Color.White)
                            }
                        }

                        Button(
                            onClick  = viewModel::requestTextReport,
                            modifier = Modifier.weight(1f),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = ArmadaColors.BtnOcr,
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Message,
                                contentDescription = null,
                                tint     = Color.White,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Share Text", color = Color.White)
                        }
                    }
                }

                // ── Report table ──────────────────────────────────────────────
                item {
                    ReportTableHeader()
                }
            }

            // ── Report rows ───────────────────────────────────────────────────
            if (selectedOutlet != null) {
                items(
                    count = reportEntries.itemCount,
                    key   = { index -> reportEntries[index]?.id ?: index.toLong() },
                ) { index ->
                    val entry = reportEntries[index]
                    if (entry != null) {
                        ReportTableRow(entry = entry, index = index)
                    }
                }

                if (reportEntries.itemCount == 0) {
                    item {
                        Text(
                            text     = "No active entries for this outlet.",
                            color    = ArmadaColors.TextSecondary,
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }
                }
            }

            // ── Past exports ──────────────────────────────────────────────────
            if (pastExports.isNotEmpty()) {
                item { Spacer(Modifier.height(4.dp)) }
                item {
                    Text(
                        "Past Exports",
                        fontWeight = FontWeight.SemiBold,
                        color      = ArmadaColors.BrandTitle,
                        fontSize   = 14.sp,
                    )
                }
                items(pastExports.size) { i ->
                    PastExportRow(
                        file         = pastExports[i],
                        context      = context,
                        onShareError = viewModel::emitSnack,
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter),
        )
    }
}

// ── Summary card ──────────────────────────────────────────────────────────────

@Composable
private fun SummaryCard(s: ReportsViewModel.SummaryData) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        shape     = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text       = s.outletName,
                fontWeight = FontWeight.Bold,
                color      = ArmadaColors.BrandTitle,
                fontSize   = 16.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = "${s.merchandiser}  ·  ${s.salesman}",
                color = ArmadaColors.TextSecondary,
                fontSize = 12.sp,
            )
            Text(
                text  = s.monthLabel,
                color = ArmadaColors.TextSecondary,
                fontSize = 12.sp,
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = ArmadaColors.Border, thickness = 0.5.dp)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                SummaryStatItem(label = "Total Items", value = s.itemCount.toString(),
                    color = ArmadaColors.BrandAccent)
                SummaryStatItem(label = "Expired", value = s.expiredCount.toString(),
                    color = ArmadaColors.StatusExpired)
                SummaryStatItem(label = "≤30 Days", value = s.within30Count.toString(),
                    color = ArmadaColors.StatusD30)
            }
        }
    }
}

@Composable
private fun SummaryStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 20.sp)
        Text(label, color = ArmadaColors.TextSecondary, fontSize = 11.sp)
    }
}

// ── Report table ──────────────────────────────────────────────────────────────

@Composable
private fun ReportTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(ArmadaColors.TableHeader)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Text("CODE",        Modifier.weight(0.9f), color = Color.White,
            fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        Text("DESCRIPTION", Modifier.weight(2f),   color = Color.White,
            fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        Text("EXPIRY",      Modifier.weight(1.1f), color = Color.White,
            fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        Text("QTY",         Modifier.weight(0.7f), color = Color.White,
            fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
    }
}

@Composable
private fun ReportTableRow(entry: ExpiryEntry, index: Int) {
    val bg = if (index % 2 == 0) ArmadaColors.BgCard else ArmadaColors.BgApp
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text     = entry.productCode ?: "—",
            modifier = Modifier.weight(0.9f),
            color    = ArmadaColors.TextSecondary,
            fontSize = 11.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text     = entry.description,
            modifier = Modifier.weight(2f),
            color    = ArmadaColors.TextPrimary,
            fontSize = 11.sp,
        )
        Text(
            text     = entry.expiryDate.toDisplayDate(),
            modifier = Modifier.weight(1.1f),
            color    = ArmadaColors.TextPrimary,
            fontSize = 11.sp,
        )
        Text(
            text     = "${entry.quantity} ${entry.unit}",
            modifier = Modifier.weight(0.7f),
            color    = ArmadaColors.TextPrimary,
            fontSize = 11.sp,
        )
    }
}

// ── Past export row ───────────────────────────────────────────────────────────

@Composable
private fun PastExportRow(
    file:         File,
    context:      android.content.Context,
    onShareError: (String) -> Unit,
) {
    val dateLabel = remember(file) {
        SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault()).format(Date(file.lastModified()))
    }
    Card(
        modifier  = Modifier.fillMaxWidth(),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape     = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.Description,
                contentDescription = null,
                tint     = ArmadaColors.BrandAccent,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = file.name,
                    color    = ArmadaColors.TextPrimary,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text     = dateLabel,
                    color    = ArmadaColors.TextSecondary,
                    fontSize = 10.sp,
                )
            }
            IconButton(
                onClick = {
                    try {
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file,
                        )
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Report"))
                    } catch (_: Exception) {
                        onShareError("Could not share file.")
                    }
                },
            ) {
                Icon(
                    Icons.Filled.IosShare,
                    contentDescription = "Share",
                    tint = ArmadaColors.BrandAccent,
                )
            }
        }
    }
}
