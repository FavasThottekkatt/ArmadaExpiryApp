package com.armada.expiryapp.ui.screens.stock

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.ui.theme.ArmadaColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockScreen(
    viewModel: StockViewModel = hiltViewModel(),
) {
    val context           = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val stockMap         by viewModel.stockMap.collectAsState()
    val searchQuery      by viewModel.searchQuery.collectAsState()
    val focusedBarcode   by viewModel.focusedBarcode.collectAsState()
    val selectedUnit     by viewModel.selectedUnit.collectAsState()
    val showClearDialog  by viewModel.showClearAllDialog.collectAsState()
    val items            = viewModel.items.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.snackMessage.collect { msg -> snackbarHostState.showSnackbar(msg) }
    }
    LaunchedEffect(Unit) {
        viewModel.shareText.collect { text ->
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                context.startActivity(Intent.createChooser(intent, "Share Stock Report"))
            } catch (_: ActivityNotFoundException) {
                snackbarHostState.showSnackbar("No app found to share.")
            }
        }
    }

    // Clear All confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissClearAll,
            title = { Text("Clear All Stock Entries?", color = ArmadaColors.BrandTitle) },
            text  = { Text("This will remove all OOS and low stock markings for this outlet and session.") },
            confirmButton = {
                TextButton(onClick = viewModel::confirmClearAll) {
                    Text("Clear All", color = ArmadaColors.StatusExpired)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissClearAll) { Text("Cancel") }
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(ArmadaColors.BgApp)) {

        if (!viewModel.hasOutlet) {
            // No outlet selected banner
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text      = "Please select an outlet in Dashboard first.",
                    color     = ArmadaColors.TextSecondary,
                    fontSize  = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            // Track numpad panel height so the list can pad its bottom accordingly
            var numpadHeightPx by remember { mutableStateOf(0) }
            val density = LocalDensity.current
            val numpadHeightDp by remember { derivedStateOf { with(density) { numpadHeightPx.toDp() } } }

            Column(modifier = Modifier.fillMaxSize()) {

                // ── Search bar (fixed at top) ─────────────────────────────────
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .background(ArmadaColors.BgCard)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = viewModel::setSearchQuery,
                        modifier      = Modifier.weight(1f),
                        placeholder   = { Text("Search product name or barcode…", fontSize = 13.sp) },
                        singleLine    = true,
                        leadingIcon   = {
                            Icon(Icons.Filled.Search, contentDescription = null,
                                tint = ArmadaColors.TextSecondary)
                        },
                        trailingIcon  = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = viewModel::clearSearch) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear search",
                                        tint = ArmadaColors.TextSecondary)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = ArmadaColors.BrandAccent,
                            unfocusedBorderColor = ArmadaColors.Border,
                        ),
                        shape = RoundedCornerShape(8.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    IconButton(onClick = viewModel::requestClearAll) {
                        Icon(
                            Icons.Filled.DeleteSweep,
                            contentDescription = "Clear all",
                            tint = ArmadaColors.TextSecondary,
                        )
                    }
                }

                // ── Column headers (fixed below search) ───────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ArmadaColors.TableHeader)
                        .padding(horizontal = 4.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("OOS",         Modifier.width(44.dp), color = Color.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 11.sp, textAlign = TextAlign.Center)
                    Text("BARCODE",     Modifier.weight(1.1f), color = Color.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                    Text("DESCRIPTION", Modifier.weight(2f),   color = Color.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                    Text("QTY",         Modifier.weight(0.9f), color = Color.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 11.sp, textAlign = TextAlign.Center)
                }

                // ── Items list — takes ALL remaining space above numpad ────────
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = numpadHeightDp),
                ) {
                    items(
                        count = items.itemCount,
                        key   = { index -> items[index]?.barcode ?: index.toString() },
                    ) { index ->
                        val item = items[index]
                        if (item != null) {
                            val state   = stockMap[item.barcode]
                            val focused = focusedBarcode == item.barcode
                            StockItemRow(
                                item     = item,
                                state    = state,
                                isFocused = focused,
                                onOosTap = { viewModel.toggleOos(item.barcode, item.description, item.productCode) },
                                onQtyTap = { viewModel.tapQtyField(item.barcode, item.description, item.productCode) },
                            )
                        }
                    }
                }
            }

            // ── Numpad panel — anchored to very bottom ────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(ArmadaColors.NumpadBg)
                    .onGloballyPositioned { numpadHeightPx = it.size.height }
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Row 1 — digits 0-9 (22sp, 50dp)
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    (0..9).forEach { digit ->
                        val active = focusedBarcode != null
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(ArmadaColors.NumpadKey)
                                .border(0.5.dp, ArmadaColors.NumpadKeyBorder, RoundedCornerShape(6.dp))
                                .alpha(if (active) 1f else 0.4f)
                                .clickable(enabled = active) { viewModel.appendDigit(digit) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text       = digit.toString(),
                                fontSize   = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color      = ArmadaColors.NumpadKeyText,
                            )
                        }
                    }
                }

                // Row 2 — PC | OUT | CTN | C | SHARE (48dp)
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    // Unit buttons
                    listOf("PC", "OUT", "CTN").forEach { unit ->
                        val isSelected = selectedUnit == unit
                        val active     = focusedBarcode != null
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) ArmadaColors.BrandAccent else ArmadaColors.NumpadKey)
                                .border(0.5.dp, ArmadaColors.NumpadKeyBorder, RoundedCornerShape(6.dp))
                                .alpha(if (active) 1f else 0.4f)
                                .clickable(enabled = active) { viewModel.setUnit(unit) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text       = unit,
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = if (isSelected) Color.White else ArmadaColors.NumpadKeyText,
                            )
                        }
                    }

                    // C button (red, single tap = clear last digit, long press = clear all)
                    val cActive = focusedBarcode != null
                    Box(
                        modifier = Modifier
                            .weight(0.8f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ArmadaColors.NumpadClear)
                            .alpha(if (cActive) 1f else 0.4f)
                            .combinedClickable(
                                enabled   = cActive,
                                onClick   = { viewModel.stockClearSingle() },
                                onLongClick = { viewModel.stockClearAll() },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text       = "C",
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color      = ArmadaColors.NumpadClearText,
                        )
                    }

                    // SHARE button
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ArmadaColors.BrandAccent)
                            .clickable { viewModel.shareReport() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text       = "SHARE",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White,
                        )
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

// ── Stock item row ────────────────────────────────────────────────────────────

@Composable
private fun StockItemRow(
    item:      Item,
    state:     StockViewModel.StockState?,
    isFocused: Boolean,
    onOosTap:  () -> Unit,
    onQtyTap:  () -> Unit,
) {
    val isOos    = state?.isOos ?: false
    val qty      = state?.quantity ?: 0
    val unit     = state?.unit ?: "PC"
    val hasQty   = qty > 0 && !isOos

    val rowBg = when {
        isOos  -> ArmadaColors.StockOosRowBg
        hasQty -> ArmadaColors.StockQtyRowBg
        else   -> Color.Unspecified
    }

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // OOS LED circle (48dp tap target)
        Box(
            modifier         = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .clickable(onClick = onOosTap),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isOos) ArmadaColors.StockOosActive else ArmadaColors.StockOosInactive),
            )
        }

        // Barcode
        Text(
            text     = item.barcode,
            modifier = Modifier.weight(1.1f),
            color    = ArmadaColors.TextSecondary,
            fontSize = 10.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        // Description (word-wrap)
        Text(
            text     = item.description,
            modifier = Modifier.weight(2f),
            color    = ArmadaColors.TextPrimary,
            fontSize = 11.sp,
        )

        // QTY field
        if (isOos) {
            // Hidden when OOS
            Spacer(Modifier.weight(0.9f))
        } else {
            Box(
                modifier = Modifier
                    .weight(0.9f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (hasQty) ArmadaColors.StockQtyBg else Color.White)
                    .border(
                        width = if (isFocused || hasQty) 1.5.dp else 0.5.dp,
                        color = when {
                            hasQty   -> ArmadaColors.StockQtyBorder
                            isFocused -> ArmadaColors.BrandAccent
                            else      -> ArmadaColors.Border
                        },
                        shape = RoundedCornerShape(4.dp),
                    )
                    .clickable(onClick = onQtyTap),
                contentAlignment = Alignment.Center,
            ) {
                if (hasQty) {
                    Text(
                        text       = "$qty $unit",
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = ArmadaColors.TextPrimary,
                        textAlign  = TextAlign.Center,
                    )
                }
            }
        }
    }
}
