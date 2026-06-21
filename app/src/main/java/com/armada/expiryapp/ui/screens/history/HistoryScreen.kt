package com.armada.expiryapp.ui.screens.history

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.toDisplayDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val context           = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedOutlet   by viewModel.selectedOutlet.collectAsState()
    val outletQuery      by viewModel.outletQuery.collectAsState()
    val outletResults    by viewModel.outletResults.collectAsState()
    val archivedCount    by viewModel.archivedCount.collectAsState()
    val isArchiving      by viewModel.isArchiving.collectAsState()
    val isExporting      by viewModel.isExporting.collectAsState()
    val showArchiveDialog by viewModel.showArchiveDialog.collectAsState()
    val entries          = viewModel.archivedEntries.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.snackMessage.collect { msg -> snackbarHostState.showSnackbar(msg) }
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
                context.startActivity(Intent.createChooser(intent, "Share History Report"))
            } catch (_: ActivityNotFoundException) {
                snackbarHostState.showSnackbar("No app found to share this file.")
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Share failed: ${e.localizedMessage ?: "error"}")
            }
        }
    }

    // ── Archive confirmation dialog ────────────────────────────────────────────
    if (showArchiveDialog) {
        val outlet = selectedOutlet
        val monthLabel = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
        AlertDialog(
            onDismissRequest = viewModel::dismissArchiveDialog,
            title = {
                Text("Archive This Month?", color = ArmadaColors.BrandTitle, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "All active expiry entries for ${outlet?.outletName ?: "this outlet"} " +
                    "entered in $monthLabel will be archived. This cannot be undone."
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::confirmArchive) {
                    Text("Archive", color = ArmadaColors.StatusExpired, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissArchiveDialog) { Text("Cancel") }
            },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArmadaColors.BgApp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Outlet selector ───────────────────────────────────────────────
            HistoryOutletSelector(
                query         = outletQuery,
                results       = outletResults,
                selectedOutlet = selectedOutlet,
                onQueryChange  = viewModel::setOutletQuery,
                onSelect       = viewModel::selectOutlet,
                onClear        = viewModel::clearOutletSelection,
            )

            // ── Action buttons ────────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .background(ArmadaColors.BgCard)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick  = viewModel::requestArchive,
                    enabled  = selectedOutlet != null && !isArchiving,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        contentColor         = ArmadaColors.StatusExpired,
                        disabledContentColor = ArmadaColors.Disabled,
                    ),
                ) {
                    if (isArchiving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp),
                            strokeWidth = 2.dp,
                            color       = ArmadaColors.StatusExpired,
                        )
                    } else {
                        Text("Archive This Month", fontSize = 12.sp)
                    }
                }

                Button(
                    onClick  = viewModel::exportHistory,
                    enabled  = selectedOutlet != null && archivedCount > 0 && !isExporting,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = ArmadaColors.BrandAccent,
                        disabledContainerColor = ArmadaColors.Disabled,
                    ),
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp),
                            strokeWidth = 2.dp,
                            color       = Color.White,
                        )
                    } else {
                        Text("Export History (XLSX)", fontSize = 12.sp)
                    }
                }
            }

            // ── Summary card ──────────────────────────────────────────────────
            if (selectedOutlet != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(
                        modifier          = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text       = selectedOutlet!!.outletName,
                                color      = ArmadaColors.BrandTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 14.sp,
                                maxLines   = 1,
                                overflow   = TextOverflow.Ellipsis,
                            )
                            Text(
                                text     = selectedOutlet!!.outletCode,
                                color    = ArmadaColors.TextSecondary,
                                fontSize = 11.sp,
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text       = "$archivedCount",
                                color      = ArmadaColors.BrandAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 22.sp,
                            )
                            Text(
                                text     = "archived",
                                color    = ArmadaColors.TextSecondary,
                                fontSize = 10.sp,
                            )
                        }
                    }
                }
            }

            // ── Column headers ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ArmadaColors.TableHeader)
                    .padding(horizontal = 6.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("CODE",        Modifier.weight(0.8f), color = Color.White,
                    fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                Text("DESCRIPTION", Modifier.weight(2f),   color = Color.White,
                    fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                Text("EXPIRY",      Modifier.weight(1f),   color = Color.White,
                    fontWeight = FontWeight.SemiBold, fontSize = 11.sp, textAlign = TextAlign.Center)
                Text("QTY",         Modifier.weight(0.6f), color = Color.White,
                    fontWeight = FontWeight.SemiBold, fontSize = 11.sp, textAlign = TextAlign.Center)
            }

            // ── Entry list or placeholder ─────────────────────────────────────
            if (selectedOutlet == null) {
                Box(
                    modifier         = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text      = "Select an outlet to view archived history.",
                        color     = ArmadaColors.TextSecondary,
                        fontSize  = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(32.dp),
                    )
                }
            } else if (entries.itemCount == 0) {
                Box(
                    modifier         = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text      = "No archived entries for this outlet.",
                        color     = ArmadaColors.TextSecondary,
                        fontSize  = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(32.dp),
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(
                        count = entries.itemCount,
                        key   = { index -> entries[index]?.id ?: index.toLong() },
                    ) { index ->
                        val entry = entries[index]
                        if (entry != null) {
                            HistoryEntryRow(entry = entry, isEven = index % 2 == 0)
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter),
        )
    }
}

// ── Outlet selector ───────────────────────────────────────────────────────────

@Composable
private fun HistoryOutletSelector(
    query:          String,
    results:        List<Outlet>,
    selectedOutlet: Outlet?,
    onQueryChange:  (String) -> Unit,
    onSelect:       (Outlet) -> Unit,
    onClear:        () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ArmadaColors.BgCard)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        OutlinedTextField(
            value         = query,
            onValueChange = onQueryChange,
            modifier      = Modifier.fillMaxWidth(),
            placeholder   = { Text("Search outlet by name or code…", fontSize = 13.sp) },
            singleLine    = true,
            leadingIcon   = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = ArmadaColors.TextSecondary)
            },
            trailingIcon  = {
                if (query.isNotBlank()) {
                    IconButton(onClick = onClear) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = ArmadaColors.TextSecondary)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = ArmadaColors.BrandAccent,
                unfocusedBorderColor = ArmadaColors.Border,
            ),
            shape = RoundedCornerShape(8.dp),
        )

        if (results.isNotEmpty() && selectedOutlet == null) {
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .padding(top = 2.dp),
                colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape     = RoundedCornerShape(8.dp),
            ) {
                results.take(5).forEach { outlet ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(outlet) }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text     = outlet.outletName,
                                color    = ArmadaColors.TextPrimary,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text     = outlet.outletCode,
                                color    = ArmadaColors.TextSecondary,
                                fontSize = 10.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── History entry row ─────────────────────────────────────────────────────────

@Composable
private fun HistoryEntryRow(entry: ExpiryEntry, isEven: Boolean) {
    val rowBg = if (isEven) Color.White else Color(0xFFF8FAFC)
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .padding(horizontal = 6.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text     = entry.productCode ?: "—",
            modifier = Modifier.weight(0.8f),
            color    = ArmadaColors.TextSecondary,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text     = entry.description,
            modifier = Modifier.weight(2f),
            color    = ArmadaColors.TextPrimary,
            fontSize = 11.sp,
        )
        Text(
            text      = entry.expiryDate.toDisplayDate(),
            modifier  = Modifier.weight(1f),
            color     = ArmadaColors.TextPrimary,
            fontSize  = 11.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text      = "${entry.quantity} ${entry.unit}",
            modifier  = Modifier.weight(0.6f),
            color     = ArmadaColors.TextPrimary,
            fontSize  = 11.sp,
            textAlign = TextAlign.Center,
        )
    }
}
