@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class,
)

package com.armada.expiryapp.ui.screens.entry

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.dotColor
import com.armada.expiryapp.util.status
import com.armada.expiryapp.util.toDisplayDate
import java.time.LocalDate

@Composable
fun NewEntryScreen(
    viewModel:   NewEntryViewModel = hiltViewModel(),
    onScanClick: () -> Unit = {},
    onOcrClick:  () -> Unit = {},
) {
    val barcode          by viewModel.barcode.collectAsState()
    val description      by viewModel.description.collectAsState()
    val productCode      by viewModel.productCode.collectAsState()
    val descriptionQuery by viewModel.descriptionQuery.collectAsState()
    val productCodeQuery by viewModel.productCodeQuery.collectAsState()
    val itemFilled       by viewModel.itemFilled.collectAsState()
    val expiryDateDisplay by viewModel.expiryDateDisplay.collectAsState()
    val isDateComplete   by viewModel.isDateComplete.collectAsState()
    val quantity         by viewModel.quantity.collectAsState()
    val unit             by viewModel.unit.collectAsState()
    val activeField      by viewModel.activeField.collectAsState()

    val descriptionResults by viewModel.descriptionResults.collectAsState()
    val isSearching        by viewModel.isSearching.collectAsState()

    val lazyPagingItems = viewModel.latestEntries.collectAsLazyPagingItems()
    val today           = remember { LocalDate.now() }

    val expiryDateRaw      by viewModel.expiryDateRaw.collectAsState()
    var showCalendarPicker by remember { mutableStateOf(false) }
    val saveDialogState    by viewModel.saveDialogState.collectAsState()
    val unlinkedItemState  by viewModel.unlinkedItemState.collectAsState()
    val editingEntry       by viewModel.editingEntry.collectAsState()
    val isBulkMode         by viewModel.isBulkMode.collectAsState()
    val showRepeatButton   by viewModel.showRepeatButton.collectAsState()

    val snackbarHostState  = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope     = rememberCoroutineScope()
    val density            = LocalDensity.current
    var numpadHeightPx     by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.snackMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArmadaColors.BgApp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Outlet banner ──────────────────────────────────────────────────
            OutletBanner(outletName = viewModel.sessionHolder.outletName)

            // ── Bulk Mode banner (only when active) ────────────────────────────
            if (isBulkMode) {
                BulkModeBanner(onTurnOff = viewModel::deactivateBulkMode)
            }

            // ── Scrollable content + table ─────────────────────────────────────
            LazyColumn(
                modifier            = Modifier
                    .weight(1f)
                    .imePadding(),
                contentPadding      = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = 12.dp,
                    bottom = 12.dp + with(density) { numpadHeightPx.toDp() },
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                item(key = "item_section") {
                    ItemSection(
                        barcode                  = barcode,
                        description              = description,
                        productCode              = productCode,
                        itemFilled               = itemFilled,
                        activeField              = activeField,
                        descriptionQuery         = descriptionQuery,
                        productCodeQuery         = productCodeQuery,
                        descriptionResults       = descriptionResults,
                        isSearching              = isSearching,
                        onBarcodeTap             = {
                            keyboardController?.hide()
                            viewModel.setActiveField(ActiveField.BARCODE)
                        },
                        onNextTap                = viewModel::lookupBarcode,
                        onScanTap                = onScanClick,
                        onDescriptionChange      = viewModel::setDescriptionQuery,
                        onDescriptionItemSelect  = viewModel::onDescriptionItemSelected,
                        onProductCodeChange      = viewModel::setProductCodeQuery,
                        onProductCodeLookup      = viewModel::lookupProductCode,
                        onClearItem              = viewModel::clearItem,
                    )
                }

                item(key = "date_section") {
                    DateSection(
                        displayValue  = expiryDateDisplay,
                        isActive      = activeField == ActiveField.DATE,
                        isComplete    = isDateComplete,
                        onFieldTap    = {
                            keyboardController?.hide()
                            viewModel.setActiveField(ActiveField.DATE)
                        },
                        onOcrTap      = {
                            keyboardController?.hide()
                            viewModel.setActiveField(ActiveField.DATE)
                            onOcrClick()
                        },
                        onCalendarTap = {
                            keyboardController?.hide()
                            viewModel.setActiveField(ActiveField.DATE)
                            showCalendarPicker = true
                        },
                    )
                }

                item(key = "qty_section") {
                    QtySection(
                        displayValue = quantity,
                        isActive     = activeField == ActiveField.QTY,
                        onFieldTap   = {
                            keyboardController?.hide()
                            viewModel.setActiveField(ActiveField.QTY)
                        },
                    )
                }

                // ── Repeat chip (after auto-save, before bulk mode) ──────────

                if (showRepeatButton) {
                    item(key = "repeat_chip") {
                        RepeatChip(onClick = viewModel::activateBulkMode)
                    }
                }

                // ── Entry data table ─────────────────────────────────────────

                if (lazyPagingItems.itemCount > 0
                    || lazyPagingItems.loadState.refresh is LoadState.Loading
                ) {
                    item(key = "table_header") { EntryTableHeader() }
                }

                items(
                    count = lazyPagingItems.itemCount,
                    key   = lazyPagingItems.itemKey { it.id },
                ) { index ->
                    val entry = lazyPagingItems[index]
                    if (entry != null) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    coroutineScope.launch {
                                        viewModel.deleteEntry(entry.id)
                                        val result = snackbarHostState.showSnackbar(
                                            message     = "Entry deleted",
                                            actionLabel = "UNDO",
                                            duration    = SnackbarDuration.Short,
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.undoDelete()
                                        } else {
                                            viewModel.clearPendingDelete()
                                        }
                                    }
                                    true
                                } else false
                            },
                        )
                        SwipeToDismissBox(
                            state                      = dismissState,
                            enableDismissFromStartToEnd = false,
                            enableDismissFromEndToStart = true,
                            backgroundContent = {
                                SwipeDeleteBackground(targetValue = dismissState.targetValue)
                            },
                        ) {
                            EntryTableRow(
                                entry       = entry,
                                index       = index,
                                today       = today,
                                onLongPress = { viewModel.startEditEntry(entry) },
                            )
                        }
                    }
                }

            }
        }

        // ── Numpad — fixed at very bottom ─────────────────────────────────────
        NumPad(
            activeField   = activeField,
            selectedUnit  = unit,
            onDigit       = viewModel::onNumpadDigit,
            onClearSingle = viewModel::onNumpadClearSingle,
            onClearAll    = viewModel::onNumpadClearAll,
            onUnitSelect  = viewModel::setUnit,
            modifier      = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { numpadHeightPx = it.size.height },
        )

        // ── Validation dialogs ─────────────────────────────────────────────────
        when (val state = saveDialogState) {
            is SaveDialogState.DuplicateFound -> AlertDialog(
                onDismissRequest = viewModel::onDuplicateDismiss,
                title   = { Text("Duplicate Entry") },
                text    = {
                    Text(
                        "${state.existing.description}\nwas already recorded for this outlet and expiry date.\n\n" +
                        "Add ${state.pending.quantity} ${state.pending.unit} to the existing entry?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onDuplicateConfirmMerge(state.existing.id, state.pending.quantity)
                    }) { Text("Add Quantity") }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::onDuplicateDismiss) { Text("Cancel") }
                },
            )
            is SaveDialogState.PastDateWarning -> AlertDialog(
                onDismissRequest = viewModel::onPastDateDismiss,
                title   = { Text("Past Expiry Date") },
                text    = { Text("The selected expiry date has already passed. Are you sure you want to continue?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.onPastDateConfirmed(state.pending) }) { Text("Continue") }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::onPastDateDismiss) { Text("Cancel") }
                },
            )
            is SaveDialogState.QtyWarning -> AlertDialog(
                onDismissRequest = viewModel::onQtyDismiss,
                title   = { Text("Large Quantity") },
                text    = {
                    Text("Quantity is ${state.pending.quantity} ${state.pending.unit} (exceeds 20). Are you sure?")
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.onQtyConfirmed(state.pending) }) { Text("Continue") }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::onQtyDismiss) { Text("Cancel") }
                },
            )
            SaveDialogState.None -> Unit
        }

        // ── Unlinked item warning dialog ───────────────────────────────────────
        val unlinkedState = unlinkedItemState
        if (unlinkedState is UnlinkedItemState.Warning) {
            AlertDialog(
                onDismissRequest = viewModel::onUnlinkedItemDismiss,
                title   = { Text("Item Not in Linked List") },
                text    = {
                    Text(
                        "\"${unlinkedState.item.description}\" is not linked to " +
                        "${viewModel.sessionHolder.outletName}. Add it anyway?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = viewModel::onUnlinkedItemConfirm) {
                        Text("Yes, Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::onUnlinkedItemDismiss) { Text("No, Cancel") }
                },
            )
        }

        // ── Edit entry dialog ──────────────────────────────────────────────────
        val entryToEdit = editingEntry
        if (entryToEdit != null) {
            EditEntryDialog(
                entry     = entryToEdit,
                onDismiss = viewModel::cancelEdit,
                onSave    = { iso, qty, unit -> viewModel.saveEditedEntry(iso, qty, unit) },
                onDelete  = {
                    viewModel.deleteEntry(entryToEdit.id)
                    viewModel.cancelEdit()
                },
            )
        }

        // ── Calendar date picker dialog ────────────────────────────────────────
        if (showCalendarPicker) {
            CalendarDatePicker(
                currentRaw     = expiryDateRaw,
                onDateSelected = { raw ->
                    viewModel.setExpiryDateFromCalendar(raw)
                    showCalendarPicker = false
                },
                onDismiss      = { showCalendarPicker = false },
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
    }
}

// ── Outlet banner ─────────────────────────────────────────────────────────────

@Composable
private fun OutletBanner(
    outletName: String,
    modifier:   Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ArmadaColors.BgHeader)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text       = outletName,
            color      = ArmadaColors.TextOnDark,
            fontWeight = FontWeight.Bold,
            fontSize   = 18.sp,
            softWrap   = true,
        )
    }
}

// ── Bulk Mode banner ──────────────────────────────────────────────────────────

@Composable
private fun BulkModeBanner(
    onTurnOff: () -> Unit,
    modifier:  Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArmadaColors.BulkModeBg)
            .drawBehind {
                drawRect(
                    color = ArmadaColors.BulkModeBorder,
                    size  = Size(4.dp.toPx(), size.height),
                )
            }
            .padding(start = 14.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text     = "🔁  BULK MODE ON — Last item pre-filled. Change if needed.",
            style    = MaterialTheme.typography.bodySmall,
            color    = Color(0xFF7D5A00),
            modifier = Modifier.weight(1f),
            softWrap = true,
        )
        TextButton(onClick = onTurnOff) {
            Text(
                text       = "Turn Off",
                color      = ArmadaColors.BulkModeBorder,
                fontWeight = FontWeight.Bold,
                style      = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

// ── Repeat chip (activates Bulk Mode after auto-save) ─────────────────────────

@Composable
private fun RepeatChip(
    onClick:  () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(ArmadaColors.BulkModeBg)
            .border(1.dp, ArmadaColors.BulkModeBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text       = "🔁  Repeat",
            style      = MaterialTheme.typography.labelMedium,
            color      = ArmadaColors.BulkModeBorder,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ── Item section (Paths A + B + C) ───────────────────────────────────────────

@Composable
private fun ItemSection(
    barcode:                 String,
    description:             String,
    productCode:             String,
    itemFilled:              Boolean,
    activeField:             ActiveField,
    descriptionQuery:        String,
    productCodeQuery:        String,
    descriptionResults:      List<Item>,
    isSearching:             Boolean,
    onBarcodeTap:            () -> Unit,
    onNextTap:               () -> Unit,
    onScanTap:               () -> Unit,
    onDescriptionChange:     (String) -> Unit,
    onDescriptionItemSelect: (Item) -> Unit,
    onProductCodeChange:     (String) -> Unit,
    onProductCodeLookup:     () -> Unit,
    onClearItem:             () -> Unit,
    modifier:                Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (itemFilled) {
            ItemFilledCard(
                description = description,
                barcode     = barcode,
                productCode = productCode,
                onClear     = onClearItem,
            )
        } else {
            // Path A + C — barcode and product code side by side
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.Top,
            ) {
                BarcodeInputRow(
                    barcode  = barcode,
                    isActive = activeField == ActiveField.BARCODE,
                    onTap    = onBarcodeTap,
                    onNext   = onNextTap,
                    onScan   = onScanTap,
                    modifier = Modifier.weight(2f),
                )
                ProductCodeInputRow(
                    query         = productCodeQuery,
                    onQueryChange = onProductCodeChange,
                    onLookup      = onProductCodeLookup,
                    modifier      = Modifier.weight(1f),
                )
            }
            // Path B — description search with live Room dropdown
            DescriptionSearchRow(
                query          = descriptionQuery,
                onQueryChange  = onDescriptionChange,
                results        = descriptionResults,
                onItemSelected = onDescriptionItemSelect,
                isSearching    = isSearching,
            )
        }
    }
}

@Composable
private fun ItemFilledCard(
    description: String,
    barcode:     String,
    productCode: String,
    onClear:     () -> Unit,
    modifier:    Modifier = Modifier,
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(8.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.FieldFilledBg),
        border    = BorderStroke(1.dp, ArmadaColors.FieldActiveBorder),
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier            = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text       = description,
                        style      = MaterialTheme.typography.bodyMedium,
                        color      = ArmadaColors.BrandTitle,
                        fontWeight = FontWeight.Bold,
                        softWrap   = true,
                        modifier   = Modifier.weight(1f),
                    )
                    Text("  ✅", style = MaterialTheme.typography.bodyMedium)
                }
                if (barcode.isNotBlank()) {
                    Text(barcode, style = MaterialTheme.typography.bodySmall, color = ArmadaColors.TextSecondary)
                }
                if (productCode.isNotBlank()) {
                    Text(productCode, style = MaterialTheme.typography.bodySmall, color = ArmadaColors.TextSecondary)
                }
            }
            IconButton(onClick = onClear) {
                Icon(Icons.Filled.Close, contentDescription = "Clear item", tint = ArmadaColors.TextSecondary)
            }
        }
    }
}

@Composable
private fun BarcodeInputRow(
    barcode:  String,
    isActive: Boolean,
    onTap:    () -> Unit,
    onNext:   () -> Unit,
    onScan:   () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isManual    = isActive && barcode.isNotBlank()
    val bgColor     = when { isManual -> ArmadaColors.FieldManualBg; isActive -> ArmadaColors.FieldActiveBg; else -> ArmadaColors.BgCard }
    val borderColor = when { isManual -> ArmadaColors.FieldManualBorder; isActive -> ArmadaColors.FieldActiveBorder; else -> ArmadaColors.Border }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (isActive) {
            Text("Entering: BARCODE", style = MaterialTheme.typography.labelSmall, color = ArmadaColors.FieldActiveLabel)
        }
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor)
                    .border(if (isActive) 2.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable(onClick = onTap)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text     = barcode.ifBlank { "Manual Barcode Entry" },
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = if (barcode.isBlank()) ArmadaColors.TextSecondary else ArmadaColors.TextPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector        = Icons.Filled.CameraAlt,
                        contentDescription = "Scan",
                        tint               = if (isActive) ArmadaColors.FieldActiveBorder else ArmadaColors.TextSecondary,
                        modifier           = Modifier
                            .size(24.dp)
                            .clickable(onClick = onScan),
                    )
                }
            }
            if (barcode.isNotBlank()) {
                Button(
                    onClick = onNext,
                    shape  = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ArmadaColors.BrandAccent),
                    modifier = Modifier.height(48.dp),
                ) {
                    Text("NEXT", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ProductCodeInputRow(
    query:         String,
    onQueryChange: (String) -> Unit,
    onLookup:      () -> Unit,
    modifier:      Modifier = Modifier,
) {
    OutlinedTextField(
        value          = query,
        onValueChange  = onQueryChange,
        label          = { Text("Enter Product Code  (Path C)", style = MaterialTheme.typography.labelSmall) },
        placeholder    = { Text("e.g. AL1004", style = MaterialTheme.typography.bodySmall, color = ArmadaColors.TextSecondary) },
        singleLine     = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onLookup() }),
        modifier       = modifier.fillMaxWidth(),
        colors         = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = ArmadaColors.FieldActiveBorder,
            unfocusedBorderColor = ArmadaColors.Border,
            focusedLabelColor    = ArmadaColors.FieldActiveLabel,
        ),
    )
}

@Composable
private fun DescriptionSearchRow(
    query:          String,
    onQueryChange:  (String) -> Unit,
    results:        List<Item>,
    onItemSelected: (Item) -> Unit,
    isSearching:    Boolean,
    modifier:       Modifier = Modifier,
) {
    val showResults = results.isNotEmpty() && query.length >= 2

    Column(modifier = modifier) {
        OutlinedTextField(
            value         = query,
            onValueChange = onQueryChange,
            label         = { Text("Select / Enter Description") },
            placeholder   = { Text("Type to search items…", color = ArmadaColors.TextSecondary) },
            trailingIcon  = {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(20.dp),
                        color       = ArmadaColors.BrandAccent,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(Icons.Filled.ArrowDropDown, null, tint = ArmadaColors.TextSecondary)
                }
            },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth(),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                unfocusedBorderColor = ArmadaColors.Border,
                focusedLabelColor    = ArmadaColors.FieldActiveLabel,
            ),
        )

        if (showResults) {
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp),
                shape     = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    results.forEachIndexed { index, item ->
                        DescriptionResultItem(
                            item     = item,
                            onSelect = { onItemSelected(item) },
                        )
                        if (index < results.lastIndex) {
                            HorizontalDivider(
                                color     = ArmadaColors.Border,
                                thickness = 0.5.dp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DescriptionResultItem(
    item:     Item,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text     = item.description,
            style    = MaterialTheme.typography.bodyMedium,
            color    = ArmadaColors.TextPrimary,
            softWrap = true,
            maxLines = Int.MAX_VALUE,
        )
        if (!item.productCode.isNullOrBlank()) {
            Text(
                text  = item.productCode,
                style = MaterialTheme.typography.bodySmall,
                color = ArmadaColors.TextSecondary,
            )
        }
    }
}

// ── Date section ──────────────────────────────────────────────────────────────

@Composable
private fun DateSection(
    displayValue:  String,
    isActive:      Boolean,
    isComplete:    Boolean,
    onFieldTap:    () -> Unit,
    onOcrTap:      () -> Unit,
    onCalendarTap: () -> Unit,
    modifier:      Modifier = Modifier,
) {
    val bgColor     = when { isComplete -> ArmadaColors.FieldFilledBg; isActive -> ArmadaColors.FieldActiveBg; else -> ArmadaColors.BgCard }
    val borderColor = when { isComplete || isActive -> ArmadaColors.FieldActiveBorder; else -> ArmadaColors.Border }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (isActive) {
            Text("Entering: DATE", style = MaterialTheme.typography.labelSmall, color = ArmadaColors.FieldActiveLabel)
        }
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor)
                    .border(if (isActive) 2.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable(onClick = onFieldTap)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text     = displayValue.ifBlank { "dd / mm / yyyy" },
                        style    = MaterialTheme.typography.bodyLarge,
                        color    = if (displayValue.isBlank()) ArmadaColors.TextSecondary else ArmadaColors.TextPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    if (isComplete) {
                        Text("✅", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.width(6.dp))
                    }
                    Icon(
                        imageVector        = Icons.Filled.CalendarToday,
                        contentDescription = "Date picker",
                        tint               = if (isActive) ArmadaColors.FieldActiveBorder else ArmadaColors.TextSecondary,
                        modifier           = Modifier
                            .size(22.dp)
                            .clickable(onClick = onCalendarTap),
                    )
                }
            }
            TextButton(
                onClick  = onOcrTap,
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text      = "📷\nOCR",
                    style     = MaterialTheme.typography.labelSmall,
                    color     = ArmadaColors.BtnOcr,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// ── QTY section ───────────────────────────────────────────────────────────────

@Composable
private fun QtySection(
    displayValue: String,
    isActive:     Boolean,
    onFieldTap:   () -> Unit,
    modifier:     Modifier = Modifier,
) {
    val bgColor     = if (isActive) ArmadaColors.FieldActiveBg else ArmadaColors.BgCard
    val borderColor = if (isActive) ArmadaColors.FieldActiveBorder else ArmadaColors.Border

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (isActive) {
            Text("Entering: QTY", style = MaterialTheme.typography.labelSmall, color = ArmadaColors.FieldActiveLabel)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .border(if (isActive) 2.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
                .clickable(onClick = onFieldTap)
                .padding(horizontal = 14.dp, vertical = 14.dp),
        ) {
            Text(
                text  = displayValue.ifBlank { "QTY" },
                style = MaterialTheme.typography.bodyLarge,
                color = if (displayValue.isBlank()) ArmadaColors.TextSecondary else ArmadaColors.TextPrimary,
                fontWeight = if (displayValue.isNotBlank()) FontWeight.Medium else FontWeight.Normal,
            )
        }
    }
}

// ── Entry table ───────────────────────────────────────────────────────────────

@Composable
private fun EntryTableHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArmadaColors.TableHeader, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Text("DESCRIPTION", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(3f))
        Text("EXPIRY",      style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text("QTY",         style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text("CODE",        style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
    }
}

@Composable
private fun EntryTableRow(
    entry:       ExpiryEntry,
    index:       Int,
    today:       LocalDate,
    onLongPress: () -> Unit,
    modifier:    Modifier = Modifier,
) {
    val bgColor = if (index % 2 == 0) ArmadaColors.BgCard else Color(0xFFF8FAFC)
    val status  = remember(entry.expiryDate, today) { entry.status(today) }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier          = modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication        = LocalIndication.current,
                onClick           = {},
                onLongClick       = onLongPress,
            )
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text      = entry.description,
            style     = MaterialTheme.typography.bodySmall,
            color     = ArmadaColors.TextPrimary,
            softWrap  = true,
            maxLines  = Int.MAX_VALUE,
            modifier  = Modifier.weight(3f),
        )
        Text(
            text       = entry.expiryDate.toDisplayDate(),
            style      = MaterialTheme.typography.bodySmall,
            color      = status.dotColor(),
            fontWeight = FontWeight.Medium,
            modifier   = Modifier.weight(1.5f),
        )
        Text(
            text     = "${entry.quantity} ${entry.unit}",
            style    = MaterialTheme.typography.bodySmall,
            color    = ArmadaColors.TextPrimary,
            modifier = Modifier.weight(1.5f),
        )
        Text(
            text     = entry.productCode ?: "—",
            style    = MaterialTheme.typography.bodySmall,
            color    = ArmadaColors.TextSecondary,
            modifier = Modifier.weight(1.5f),
        )
    }
}

// ── Swipe-delete background ───────────────────────────────────────────────────

@Composable
private fun SwipeDeleteBackground(
    targetValue: SwipeToDismissBoxValue,
    modifier:    Modifier = Modifier,
) {
    val bgColor by animateColorAsState(
        targetValue = if (targetValue == SwipeToDismissBoxValue.EndToStart)
            Color(0xFFD32F2F) else Color(0xFFEF9A9A),
        label = "swipeDeleteBg",
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(end = 20.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector        = Icons.Filled.Delete,
            contentDescription = "Delete",
            tint               = Color.White,
        )
    }
}

// ── Edit entry dialog ─────────────────────────────────────────────────────────

@Composable
private fun EditEntryDialog(
    entry:     ExpiryEntry,
    onDismiss: () -> Unit,
    onSave:    (newExpiryIso: String, newQty: Int, newUnit: String) -> Unit,
    onDelete:  () -> Unit,
) {
    val initialRaw = remember(entry.expiryDate) {
        runCatching {
            val p = entry.expiryDate.split("-")
            if (p.size == 3) "${p[2]}${p[1]}${p[0]}" else ""
        }.getOrDefault("")
    }

    var dateRaw          by remember { mutableStateOf(initialRaw) }
    var dateDisplay      by remember { mutableStateOf(NewEntryViewModel.formatDateDisplay(initialRaw)) }
    var qtyText          by remember { mutableStateOf(entry.quantity.toString()) }
    var selectedUnit     by remember { mutableStateOf(entry.unit) }
    var showCalendar     by remember { mutableStateOf(false) }
    var dateError        by remember { mutableStateOf(false) }
    var qtyError         by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showCalendar) {
        CalendarDatePicker(
            currentRaw     = dateRaw,
            onDateSelected = { raw ->
                dateRaw     = raw
                dateDisplay = NewEntryViewModel.formatDateDisplay(raw)
                dateError   = false
                showCalendar = false
            },
            onDismiss = { showCalendar = false },
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete this entry?") },
            text  = {
                Text(
                    text     = entry.description,
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = ArmadaColors.TextPrimary,
                    softWrap = true,
                    maxLines = Int.MAX_VALUE,
                )
            },
            confirmButton = {
                TextButton(onClick = onDelete) {
                    Text("Yes, Delete", color = ArmadaColors.FieldManualBorder, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("No") }
            },
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Entry") },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text       = entry.description,
                    style      = MaterialTheme.typography.bodyMedium,
                    color      = ArmadaColors.BrandTitle,
                    fontWeight = FontWeight.Bold,
                    softWrap   = true,
                    maxLines   = Int.MAX_VALUE,
                )
                OutlinedTextField(
                    value         = dateDisplay,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Expiry Date") },
                    isError       = dateError,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                        unfocusedBorderColor = if (dateError) ArmadaColors.FieldManualBorder else ArmadaColors.Border,
                    ),
                    trailingIcon = {
                        IconButton(onClick = { showCalendar = true }) {
                            Icon(Icons.Filled.CalendarToday, "Pick date", tint = ArmadaColors.BrandAccent)
                        }
                    },
                )
                OutlinedTextField(
                    value         = qtyText,
                    onValueChange = { v ->
                        if (v.all { it.isDigit() } && v.length <= 5) {
                            qtyText  = v
                            qtyError = false
                        }
                    },
                    label          = { Text("Quantity") },
                    isError        = qtyError,
                    singleLine     = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction    = ImeAction.Done,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors   = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                        unfocusedBorderColor = if (qtyError) ArmadaColors.FieldManualBorder else ArmadaColors.Border,
                    ),
                )
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    listOf("PC", "OUT", "CTN").forEach { u ->
                        Button(
                            onClick  = { selectedUnit = u },
                            modifier = Modifier.weight(1f),
                            shape    = RoundedCornerShape(6.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = if (selectedUnit == u) ArmadaColors.BrandAccent else ArmadaColors.NumpadKey,
                                contentColor   = if (selectedUnit == u) Color.White else ArmadaColors.NumpadKeyText,
                            ),
                            border = if (selectedUnit == u) null else BorderStroke(1.dp, ArmadaColors.NumpadKeyBorder),
                        ) {
                            Text(u, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val qty = qtyText.toIntOrNull() ?: 0
                if (dateRaw.length != 8) { dateError = true; return@TextButton }
                if (qty <= 0)            { qtyError  = true; return@TextButton }
                val iso = "${dateRaw.drop(4)}-${dateRaw.substring(2, 4)}-${dateRaw.take(2)}"
                onSave(iso, qty, selectedUnit)
            }) {
                Text("Save", color = ArmadaColors.BrandAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { showDeleteConfirm = true }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        },
    )
}

// ── Numpad ────────────────────────────────────────────────────────────────────

@Composable
private fun NumPad(
    activeField:   ActiveField,
    selectedUnit:  String,
    onDigit:       (Int) -> Unit,
    onClearSingle: () -> Unit,
    onClearAll:    () -> Unit,
    onUnitSelect:  (String) -> Unit,
    modifier:      Modifier = Modifier,
) {
    val view        = LocalView.current
    val numActive   = activeField != ActiveField.NONE

    Column(
        modifier            = modifier
            .fillMaxWidth()
            .background(ArmadaColors.NumpadBg)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Row 1: 0–9 — bigger, dominant row
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            (0..9).forEach { digit ->
                NumpadKey(
                    label    = digit.toString(),
                    onClick  = {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onDigit(digit)
                    },
                    enabled  = numActive,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Row 2: PC / OUT / CTN (dimmed when not QTY) + C key (always active)
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Row(
                modifier              = Modifier.weight(3f),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                listOf("PC", "OUT", "CTN").forEach { unitLabel ->
                    UnitKey(
                        label      = unitLabel,
                        isSelected = selectedUnit == unitLabel,
                        onClick    = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            onUnitSelect(unitLabel)
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            NumpadKey(
                label       = "C",
                onClick     = {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    onClearSingle()
                },
                onLongClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    onClearAll()
                },
                isClearKey  = true,
                enabled     = numActive,
                keyHeight   = 48.dp,
                fontSize    = 18.sp,
                modifier    = Modifier.weight(1.5f),
            )
        }
    }
}

@Composable
private fun NumpadKey(
    label:       String,
    onClick:     () -> Unit,
    enabled:     Boolean      = true,
    isClearKey:  Boolean      = false,
    onLongClick: (() -> Unit)? = null,
    keyHeight:   Dp           = 52.dp,
    fontSize:    TextUnit     = 24.sp,
    modifier:    Modifier     = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.93f else 1f, label = "keyScale")

    Surface(
        modifier = modifier
            .scale(scale)
            .height(keyHeight)
            .combinedClickable(
                interactionSource = interactionSource,
                indication        = LocalIndication.current,
                enabled           = enabled,
                onClick           = onClick,
                onLongClick       = onLongClick,
            ),
        shape           = RoundedCornerShape(6.dp),
        color           = if (isClearKey) ArmadaColors.NumpadClear else ArmadaColors.NumpadKey,
        border          = BorderStroke(1.dp, ArmadaColors.NumpadKeyBorder),
        shadowElevation = 2.dp,
        tonalElevation  = 0.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text       = label,
                fontSize   = fontSize,
                fontWeight = FontWeight.Bold,
                color      = if (isClearKey) ArmadaColors.NumpadClearText else ArmadaColors.NumpadKeyText,
            )
        }
    }
}

@Composable
private fun UnitKey(
    label:      String,
    isSelected: Boolean,
    onClick:    () -> Unit,
    modifier:   Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "unitScale")

    Surface(
        modifier = modifier
            .scale(scale)
            .height(48.dp)
            .clickable(interactionSource = interactionSource, indication = LocalIndication.current, onClick = onClick),
        shape           = RoundedCornerShape(6.dp),
        color           = if (isSelected) ArmadaColors.BrandAccent else ArmadaColors.NumpadKey,
        border          = BorderStroke(1.dp, if (isSelected) ArmadaColors.BrandAccent else ArmadaColors.NumpadKeyBorder),
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        tonalElevation  = 0.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text       = label,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = if (isSelected) Color.White else ArmadaColors.NumpadKeyText,
            )
        }
    }
}
