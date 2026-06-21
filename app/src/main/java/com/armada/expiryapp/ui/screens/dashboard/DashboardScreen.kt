package com.armada.expiryapp.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.ui.theme.ArmadaColors
import com.armada.expiryapp.util.Status
import com.armada.expiryapp.util.dotColor
import com.armada.expiryapp.util.status
import com.armada.expiryapp.util.toDisplayDate
import java.time.LocalDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel:          DashboardViewModel = hiltViewModel(),
    onNextClick:        () -> Unit,
    onViewArchiveClick: () -> Unit = {},
) {
    val merchandiser     by viewModel.merchandiser.collectAsState()
    val salesman         by viewModel.salesman.collectAsState()
    val outletName       by viewModel.outletName.collectAsState()
    val outletCode       by viewModel.outletCode.collectAsState()
    val outletResults    by viewModel.outletResults.collectAsState()
    val showFieldErrors  by viewModel.showFieldErrors.collectAsState()
    val isFormComplete   by viewModel.isFormComplete.collectAsState()
    val stats            by viewModel.stats.collectAsState()
    val sessionRecovery  by viewModel.sessionRecovery.collectAsState()

    val lazyPagingItems = viewModel.latestEntries.collectAsLazyPagingItems()
    val today           = remember { LocalDate.now() }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArmadaColors.BgApp),
    ) {
        LazyColumn(
            modifier            = Modifier.fillMaxSize(),
            contentPadding      = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp, vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            // ── Session recovery banner ───────────────────────────────────────

            sessionRecovery?.let { recovery ->
                item(key = "session_recovery") {
                    SessionRecoveryBanner(
                        outletName  = recovery.outletName,
                        entryCount  = recovery.entryCount,
                        onContinue  = { viewModel.continueSession(recovery) },
                        onStartFresh = viewModel::dismissSessionRecovery,
                    )
                }
            }

            item(key = "merchandiser_field") {
                SearchableDropdown(
                    label             = "Merchandiser Name *",
                    value             = merchandiser,
                    onValueChange     = viewModel::setMerchandiser,
                    allSuggestions    = DashboardViewModel.MERCHANDISERS,
                    onSuggestionClick = viewModel::setMerchandiser,
                    isError           = showFieldErrors && merchandiser.isBlank(),
                    errorLabel        = "Merchandiser is required",
                )
            }

            item(key = "salesman_field") {
                SearchableDropdown(
                    label             = "Salesman Name *",
                    value             = salesman,
                    onValueChange     = viewModel::setSalesman,
                    allSuggestions    = DashboardViewModel.SALESMEN,
                    onSuggestionClick = viewModel::setSalesman,
                    isError           = showFieldErrors && salesman.isBlank(),
                    errorLabel        = "Salesman is required",
                )
            }

            item(key = "outlet_name_field") {
                OutletSearchField(
                    value            = outletName,
                    onValueChange    = viewModel::setOutletQuery,
                    results          = outletResults,
                    onOutletSelected = viewModel::selectOutlet,
                    isError          = showFieldErrors && outletName.isBlank(),
                )
            }

            item(key = "outlet_code_field") {
                OutlinedTextField(
                    value         = outletCode,
                    onValueChange = viewModel::setOutletCode,
                    label         = { Text("Outlet Code") },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                        unfocusedBorderColor = ArmadaColors.Border,
                        focusedLabelColor    = ArmadaColors.FieldActiveLabel,
                    ),
                )
            }

            item(key = "next_button") {
                Button(
                    onClick = {
                        if (viewModel.onNextTapped()) onNextClick()
                        else scope.launch {
                            snackbarHostState.showSnackbar("Please complete all required fields.")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape  = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFormComplete) ArmadaColors.BtnNext else ArmadaColors.Disabled,
                        contentColor   = if (isFormComplete) Color.White else ArmadaColors.DisabledText,
                    ),
                ) {
                    Text("Next →", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }

            item(key = "stat_cards") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val hasOutlet = outletCode.isNotBlank()
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard(
                            modifier  = Modifier.weight(1f),
                            label     = "EXPIRED",
                            count     = stats.expired,
                            color     = ArmadaColors.StatusExpired,
                            hasOutlet = hasOutlet,
                        )
                        StatCard(
                            modifier  = Modifier.weight(1f),
                            label     = "≤30 DAYS",
                            count     = stats.within30,
                            color     = ArmadaColors.StatusD30,
                            hasOutlet = hasOutlet,
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard(
                            modifier  = Modifier.weight(1f),
                            label     = "31–60 DAYS",
                            count     = stats.within60,
                            color     = ArmadaColors.StatusD60,
                            hasOutlet = hasOutlet,
                        )
                        StatCard(
                            modifier  = Modifier.weight(1f),
                            label     = "61–90 DAYS",
                            count     = stats.within90,
                            color     = ArmadaColors.StatusD90,
                            hasOutlet = hasOutlet,
                        )
                    }
                }
            }

            item(key = "records_header") {
                Row(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment   = Alignment.CenterVertically,
                ) {
                    Text(
                        text       = "Latest Records",
                        style      = MaterialTheme.typography.titleMedium,
                        color      = ArmadaColors.BrandTitle,
                        fontWeight = FontWeight.Bold,
                    )
                    TextButton(onClick = onViewArchiveClick) {
                        Text(
                            text  = "View Archive",
                            style = MaterialTheme.typography.labelMedium,
                            color = ArmadaColors.BrandAccent,
                        )
                    }
                }
            }

            // Refresh loading state
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                item(key = "records_loading") {
                    Box(
                        modifier        = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = ArmadaColors.BrandAccent,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            }

            // Empty state
            if (lazyPagingItems.itemCount == 0
                && lazyPagingItems.loadState.refresh is LoadState.NotLoading
            ) {
                item(key = "records_empty") {
                    Box(
                        modifier        = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text  = if (outletCode.isBlank()) "Select an outlet to see entries."
                                    else "No entries found.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArmadaColors.TextSecondary,
                        )
                    }
                }
            }

            // Paged record cards
            items(
                count = lazyPagingItems.itemCount,
                key   = lazyPagingItems.itemKey { it.id },
            ) { index ->
                val entry = lazyPagingItems[index]
                if (entry != null) {
                    EntryRecordCard(entry = entry, today = today)
                }
            }

            // Append loading indicator
            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                item(key = "append_loading") {
                    Box(
                        modifier        = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color    = ArmadaColors.BrandAccent,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                        )
                    }
                }
            }

            item(key = "bottom_space") { Spacer(modifier = Modifier.height(72.dp)) }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
    }
}

// ── Private composables ───────────────────────────────────────────────────────

@Composable
private fun StatCard(
    label:     String,
    count:     Int,
    color:     Color,
    hasOutlet: Boolean,
    modifier:  Modifier = Modifier,
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = color,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    ),
            )
            Column(
                modifier            = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text       = label,
                    style      = MaterialTheme.typography.labelSmall,
                    color      = ArmadaColors.TextSecondary,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text       = if (hasOutlet) count.toString() else "—",
                    style      = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color      = if (hasOutlet) color else ArmadaColors.TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun EntryRecordCard(
    entry:    ExpiryEntry,
    today:    LocalDate,
    modifier: Modifier = Modifier,
) {
    val entryStatus = remember(entry.expiryDate, today) { entry.status(today) }
    val dot         = entryStatus.dotColor()

    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(8.dp),
        colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dot),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text       = entry.description,
                    style      = MaterialTheme.typography.bodyMedium,
                    color      = ArmadaColors.TextPrimary,
                    fontWeight = FontWeight.Medium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text       = entry.expiryDate.toDisplayDate(),
                        style      = MaterialTheme.typography.bodySmall,
                        color      = dot,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text  = "${entry.quantity} ${entry.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArmadaColors.TextSecondary,
                    )
                }
            }
            IconButton(onClick = { /* Phase 12: edit / delete actions */ }) {
                Icon(
                    imageVector        = Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    tint               = ArmadaColors.TextSecondary,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchableDropdown(
    label:             String,
    value:             String,
    onValueChange:     (String) -> Unit,
    allSuggestions:    List<String>,
    onSuggestionClick: (String) -> Unit,
    isError:           Boolean,
    errorLabel:        String,
    modifier:          Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val filtered = remember(value, allSuggestions) {
        if (value.isBlank()) allSuggestions
        else allSuggestions.filter { it.contains(value, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        expanded         = expanded && filtered.isNotEmpty(),
        onExpandedChange = { expanded = it },
        modifier         = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value         = value,
            onValueChange = { onValueChange(it); expanded = true },
            label         = { Text(label) },
            trailingIcon  = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && filtered.isNotEmpty())
            },
            singleLine     = true,
            isError        = isError,
            supportingText = if (isError) ({ Text(errorLabel, color = Color(0xFFD32F2F)) }) else null,
            modifier       = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = if (isError) OutlinedTextFieldDefaults.colors(
                errorBorderColor = Color(0xFFD32F2F),
                errorLabelColor  = Color(0xFFD32F2F),
            ) else OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                unfocusedBorderColor = ArmadaColors.Border,
                focusedLabelColor    = ArmadaColors.FieldActiveLabel,
            ),
        )
        ExposedDropdownMenu(
            expanded         = expanded && filtered.isNotEmpty(),
            onDismissRequest = { expanded = false },
        ) {
            filtered.take(15).forEach { item ->
                DropdownMenuItem(
                    text    = { Text(item, style = MaterialTheme.typography.bodyMedium) },
                    onClick = { onSuggestionClick(item); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun SessionRecoveryBanner(
    outletName:   String,
    entryCount:   Int,
    onContinue:   () -> Unit,
    onStartFresh: () -> Unit,
    modifier:     Modifier = Modifier,
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(8.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        border    = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF39C12)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier            = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text       = "⚠️ Unfinished Session",
                style      = MaterialTheme.typography.labelMedium,
                color      = Color(0xFFE65100),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text  = "You have $entryCount ${if (entryCount == 1) "entry" else "entries"} from a previous session at $outletName. Continue?",
                style = MaterialTheme.typography.bodySmall,
                color = ArmadaColors.TextPrimary,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onContinue,
                    colors  = ButtonDefaults.buttonColors(containerColor = ArmadaColors.BrandAccent),
                    modifier = Modifier.weight(1f),
                ) { Text("Continue", color = Color.White) }
                Button(
                    onClick = onStartFresh,
                    colors  = ButtonDefaults.buttonColors(containerColor = ArmadaColors.BtnNewEntryInactive),
                    modifier = Modifier.weight(1f),
                ) { Text("Start Fresh", color = ArmadaColors.TextPrimary) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutletSearchField(
    value:            String,
    onValueChange:    (String) -> Unit,
    results:          List<Outlet>,
    onOutletSelected: (Outlet) -> Unit,
    isError:          Boolean,
    modifier:         Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded         = expanded && results.isNotEmpty(),
        onExpandedChange = { expanded = it },
        modifier         = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value         = value,
            onValueChange = { onValueChange(it); if (it.isNotEmpty()) expanded = true },
            label         = { Text("Outlet Name *") },
            placeholder   = { Text("Type to search outlets…", color = ArmadaColors.TextSecondary) },
            trailingIcon  = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && results.isNotEmpty())
            },
            singleLine     = true,
            isError        = isError,
            supportingText = if (isError) ({ Text("Outlet is required", color = Color(0xFFD32F2F)) }) else null,
            modifier       = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = if (isError) OutlinedTextFieldDefaults.colors(
                errorBorderColor = Color(0xFFD32F2F),
                errorLabelColor  = Color(0xFFD32F2F),
            ) else OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                unfocusedBorderColor = ArmadaColors.Border,
                focusedLabelColor    = ArmadaColors.FieldActiveLabel,
            ),
        )
        ExposedDropdownMenu(
            expanded         = expanded && results.isNotEmpty(),
            onDismissRequest = { expanded = false },
        ) {
            results.forEach { outlet ->
                DropdownMenuItem(
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(outlet.outletName, style = MaterialTheme.typography.bodyMedium, softWrap = true)
                            Text(outlet.outletCode, style = MaterialTheme.typography.bodySmall, color = ArmadaColors.TextSecondary)
                        }
                    },
                    onClick = { onOutletSelected(outlet); expanded = false },
                )
            }
        }
    }
}
