package com.armada.expiryapp.ui.screens.itemlinking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.data.db.entity.TeamLink
import com.armada.expiryapp.ui.theme.ArmadaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLinkingScreen(
    viewModel: ItemLinkingViewModel = hiltViewModel(),
    onBack:    () -> Unit,
) {
    val searchQuery        by viewModel.searchQuery.collectAsState()
    val linkedBarcodes     by viewModel.linkedBarcodes.collectAsState()
    val showClearDialog    by viewModel.showClearDialog.collectAsState()
    val linkedOutlets      by viewModel.linkedOutlets.collectAsState()
    val selectedOutletCode by viewModel.selectedOutletCode.collectAsState()
    val selectedOutletName by viewModel.selectedOutletName.collectAsState()
    val items              = viewModel.items.collectAsLazyPagingItems()
    val snackbarState      = remember { SnackbarHostState() }

    val outletSelected = selectedOutletCode.isNotBlank()

    LaunchedEffect(Unit) {
        viewModel.snackMessage.collect { msg -> snackbarState.showSnackbar(msg) }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissClearAll,
            title   = { Text("Clear All Links?") },
            text    = { Text("Remove all item links for $selectedOutletName?") },
            confirmButton = {
                TextButton(onClick = viewModel::confirmClearAll) {
                    Text("Clear All", color = ArmadaColors.StatusExpired, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissClearAll) { Text("Cancel") }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ArmadaColors.TextOnDark,
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            "ITEM LINKING",
                            color      = ArmadaColors.TextOnDark,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                        )
                        if (outletSelected) {
                            Text(
                                selectedOutletName,
                                color    = ArmadaColors.TextOnDark.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ArmadaColors.BgHeader),
            )
        },
        snackbarHost  = { SnackbarHost(snackbarState) },
        containerColor = ArmadaColors.BgApp,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // ── Outlet selector ───────────────────────────────────────────────
            OutletDropdown(
                linkedOutlets      = linkedOutlets,
                selectedOutletCode = selectedOutletCode,
                selectedOutletName = selectedOutletName,
                onSelect           = { link -> viewModel.selectOutlet(link.outletCode, link.outletName) },
            )

            if (!outletSelected) {
                // ── No outlet selected yet ─────────────────────────────────────
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text      = "Select an outlet above to manage item links.",
                        color     = ArmadaColors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(32.dp),
                    )
                }
            } else {
                // ── Outlet selected — show full linking UI ─────────────────────

                // Search bar
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier      = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    placeholder   = { Text("Search items by name or barcode…", fontSize = 13.sp) },
                    singleLine    = true,
                    leadingIcon   = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = ArmadaColors.TextSecondary,
                        )
                    },
                    trailingIcon  = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Clear",
                                    tint = ArmadaColors.TextSecondary,
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = ArmadaColors.BrandAccent,
                        unfocusedBorderColor = ArmadaColors.Border,
                    ),
                    shape = RoundedCornerShape(8.dp),
                )

                // Action buttons
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick  = viewModel::linkSelected,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.outlinedButtonColors(
                            contentColor = ArmadaColors.BrandAccent,
                        ),
                    ) {
                        Text("Link Selected", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick  = viewModel::requestClearAll,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Clear All Links", fontSize = 12.sp, color = ArmadaColors.StatusExpired)
                    }
                }

                // Column headers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ArmadaColors.TableHeader)
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "✓", Modifier.size(32.dp),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        fontSize = 14.sp, textAlign = TextAlign.Center,
                    )
                    Text(
                        "DESCRIPTION", Modifier.weight(1f),
                        color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp,
                    )
                    Text(
                        "CODE", Modifier.padding(start = 8.dp),
                        color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp,
                    )
                }

                // Items list
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = items.itemCount,
                        key   = { i -> items[i]?.barcode ?: i.toString() },
                    ) { index ->
                        val item = items[index]
                        if (item != null) {
                            ItemLinkRow(
                                item     = item,
                                isLinked = item.barcode in linkedBarcodes,
                                onToggle = { viewModel.toggleLink(item) },
                            )
                            HorizontalDivider(color = ArmadaColors.Border, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}

// ── Private composables ───────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutletDropdown(
    linkedOutlets:      List<TeamLink>,
    selectedOutletCode: String,
    selectedOutletName: String,
    onSelect:           (TeamLink) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (selectedOutletCode.isBlank()) "" else selectedOutletName

    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = { if (linkedOutlets.isNotEmpty()) expanded = it },
        modifier         = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        OutlinedTextField(
            value         = displayText,
            onValueChange = {},
            readOnly      = true,
            label         = { Text("Select Outlet") },
            placeholder   = { Text("Tap to select a linked outlet…") },
            trailingIcon  = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = ArmadaColors.BrandAccent,
                unfocusedBorderColor = ArmadaColors.Border,
                focusedLabelColor    = ArmadaColors.BrandAccent,
            ),
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
        ) {
            if (linkedOutlets.isEmpty()) {
                DropdownMenuItem(
                    text    = { Text("No linked outlets. Complete Team Linking first.", fontSize = 13.sp) },
                    onClick = { expanded = false },
                )
            } else {
                linkedOutlets.forEach { link ->
                    DropdownMenuItem(
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(link.outletName, fontSize = 14.sp)
                                Text(link.outletCode, fontSize = 12.sp, color = ArmadaColors.TextSecondary)
                            }
                        },
                        onClick = {
                            onSelect(link)
                            expanded = false
                        },
                        modifier = if (link.outletCode == selectedOutletCode)
                            Modifier.background(ArmadaColors.FieldFilledBg)
                        else Modifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemLinkRow(
    item:     Item,
    isLinked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .background(if (isLinked) ArmadaColors.FieldFilledBg else Color.Unspecified)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector        = if (isLinked) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
            contentDescription = if (isLinked) "Linked" else "Not linked",
            tint               = if (isLinked) ArmadaColors.BrandAccent else ArmadaColors.Border,
            modifier           = Modifier.size(28.dp),
        )
        Text(
            text     = item.description,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            color    = ArmadaColors.TextPrimary,
            fontSize = 13.sp,
            softWrap = true,
            maxLines = Int.MAX_VALUE,
        )
        Text(
            text  = item.productCode ?: "—",
            color = ArmadaColors.TextSecondary,
            fontSize = 11.sp,
        )
    }
}
