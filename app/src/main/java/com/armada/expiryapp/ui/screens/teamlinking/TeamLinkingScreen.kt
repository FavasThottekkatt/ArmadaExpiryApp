package com.armada.expiryapp.ui.screens.teamlinking

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.data.db.entity.TeamLink
import com.armada.expiryapp.ui.theme.ArmadaColors

private val BrandRed = Color(0xFFCC0000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamLinkingScreen(
    onBack: () -> Unit,
    viewModel: TeamLinkingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbarState = remember { SnackbarHostState() }

    val deviceLock         by viewModel.deviceLock.collectAsState()
    val teamLinks          by viewModel.teamLinks.collectAsState()
    val isLinkReady        by viewModel.isLinkReady.collectAsState()
    val isDoneEnabled      by viewModel.isDoneEnabled.collectAsState()
    val merchandiserInput  by viewModel.merchandiserInput.collectAsState()
    val salesmanInput      by viewModel.salesmanInput.collectAsState()
    val outletQuery        by viewModel.outletQuery.collectAsState()
    val selectedOutletCode by viewModel.selectedOutletCode.collectAsState()
    val outletResults      by viewModel.outletResults.collectAsState()
    val showDoneConfirm    by viewModel.showDoneConfirm.collectAsState()
    val deleteConfirmLink  by viewModel.deleteConfirmLink.collectAsState()
    val showBackWarning    by viewModel.showBackWarning.collectAsState()

    val isLocked = deviceLock != null
    val lockedMerchandiserName = deviceLock?.merchandiserName ?: ""

    // Intercept back press when linking has started
    BackHandler(enabled = teamLinks.isNotEmpty() || isLocked) {
        viewModel.requestBackWarning()
    }

    // Navigate back after DONE confirmed
    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect { onBack() }
    }

    // Snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackMessage.collect { msg ->
            snackbarState.showSnackbar(msg)
        }
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    if (showBackWarning) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBackWarning() },
            title   = { Text("Team Linking Incomplete") },
            text    = {
                Text(
                    "Team Linking is not complete. You cannot use the app without completing this. " +
                    "Do you want to continue linking?"
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissBackWarning() }) {
                    Text("CONTINUE LINKING")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissBackWarning()
                        (context as? Activity)?.finishAndRemoveTask()
                    }
                ) {
                    Text("EXIT APP", color = MaterialTheme.colorScheme.error)
                }
            },
        )
    }

    if (showDoneConfirm) {
        val count = teamLinks.size
        AlertDialog(
            onDismissRequest = { viewModel.dismissDone() },
            title   = { Text("Confirm Team Linking") },
            text    = {
                Text(
                    "You have linked $count outlet${if (count != 1) "s" else ""}. " +
                    "Once confirmed, $lockedMerchandiserName will be permanently set as the " +
                    "merchandiser for this device. Are you sure you want to continue?"
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.confirmDone() }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDone() }) { Text("Cancel") }
            },
        )
    }

    if (deleteConfirmLink != null) {
        val link = deleteConfirmLink!!
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteLink() },
            title   = { Text("Remove Outlet?") },
            text    = { Text("Remove '${link.outletName}' (${link.outletCode}) from your linked outlets?") },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDeleteLink() },
                    colors  = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) { Text("Remove") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteLink() }) { Text("Cancel") }
            },
        )
    }

    // ── Main scaffold ─────────────────────────────────────────────────────────

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TEAM LINKING", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (teamLinks.isEmpty() && !isLocked) onBack()
                        else viewModel.requestBackWarning()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = BrandRed,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarState) },
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            // ── Merchandiser field ─────────────────────────────────────────────
            item {
                if (isLocked) {
                    OutlinedTextField(
                        value          = lockedMerchandiserName,
                        onValueChange  = {},
                        label          = { Text("Merchandiser (locked)") },
                        readOnly       = true,
                        trailingIcon   = {
                            Icon(
                                Icons.Filled.Lock,
                                contentDescription = null,
                                tint = ArmadaColors.TextSecondary,
                            )
                        },
                        modifier       = Modifier.fillMaxWidth(),
                    )
                } else {
                    AutocompleteField(
                        label             = "Merchandiser *",
                        value             = merchandiserInput,
                        onValueChange     = viewModel::setMerchandiserInput,
                        suggestions       = TeamLinkingViewModel.MERCHANDISERS.filter {
                            it.contains(merchandiserInput, ignoreCase = true)
                        },
                        onSuggestionSelected = { viewModel.setMerchandiserInput(it) },
                    )
                }
            }

            // ── Salesman field ─────────────────────────────────────────────────
            item {
                AutocompleteField(
                    label             = "Salesman *",
                    value             = salesmanInput,
                    onValueChange     = viewModel::setSalesmanInput,
                    suggestions       = TeamLinkingViewModel.SALESMEN.filter {
                        it.contains(salesmanInput, ignoreCase = true)
                    },
                    onSuggestionSelected = { viewModel.setSalesmanInput(it) },
                )
            }

            // ── Outlet search field ────────────────────────────────────────────
            item {
                OutletSearchField(
                    query             = outletQuery,
                    onQueryChange     = viewModel::setOutletQuery,
                    results           = outletResults,
                    onOutletSelected  = viewModel::selectOutlet,
                )
            }

            // ── Outlet code ────────────────────────────────────────────────────
            item {
                OutlinedTextField(
                    value         = selectedOutletCode,
                    onValueChange = {},
                    label         = { Text("Outlet Code (auto-filled)") },
                    readOnly      = true,
                    modifier      = Modifier.fillMaxWidth(),
                )
            }

            // ── LINK button ────────────────────────────────────────────────────
            item {
                Button(
                    onClick  = { viewModel.tapLink() },
                    enabled  = isLinkReady,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = Color(0xFF1B8C3E),
                        disabledContainerColor = ArmadaColors.Disabled,
                    ),
                ) {
                    Text("LINK", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // ── Linked outlets header ──────────────────────────────────────────
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "LINKED OUTLETS (${teamLinks.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 13.sp,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                HorizontalDivider()
            }

            if (teamLinks.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "No outlets linked yet. Fill the fields above and tap LINK.",
                            color    = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                        )
                    }
                }
            } else {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(ArmadaColors.FieldFilledBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text("Outlet",   Modifier.weight(2f),   fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Code",     Modifier.weight(1f),   fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Salesman", Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(Modifier.width(36.dp))
                    }
                    HorizontalDivider()
                }
                items(items = teamLinks, key = { link -> link.id }) { link ->
                    TeamLinkRow(link = link, onDelete = { viewModel.requestDeleteLink(link) })
                    HorizontalDivider()
                }
            }

            // ── DONE button ────────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick  = { viewModel.tapDone() },
                    enabled  = isDoneEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = BrandRed,
                        disabledContainerColor = ArmadaColors.Disabled,
                    ),
                ) {
                    Text("DONE — CONFIRM TEAM LINKING", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ── Private composables ───────────────────────────────────────────────────────

@Composable
private fun TeamLinkRow(link: TeamLink, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(link.outletName,   Modifier.weight(2f),   fontSize = 12.sp, maxLines = 2)
        Text(link.outletCode,   Modifier.weight(1f),   fontSize = 12.sp)
        Text(link.salesmanName, Modifier.weight(1.5f), fontSize = 12.sp)
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Remove",
                tint     = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun AutocompleteField(
    label:                String,
    value:                String,
    onValueChange:        (String) -> Unit,
    suggestions:          List<String>,
    onSuggestionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value         = value,
            onValueChange = {
                onValueChange(it)
                expanded = it.isNotBlank() && suggestions.isNotEmpty()
            },
            label      = { Text(label) },
            modifier   = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        DropdownMenu(
            expanded         = expanded && suggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier         = Modifier.fillMaxWidth(),
        ) {
            suggestions.forEach { name ->
                DropdownMenuItem(
                    text    = { Text(name, fontSize = 14.sp) },
                    onClick = {
                        onSuggestionSelected(name)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun OutletSearchField(
    query:            String,
    onQueryChange:    (String) -> Unit,
    results:          List<Outlet>,
    onOutletSelected: (Outlet) -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value         = query,
            onValueChange = onQueryChange,
            label         = { Text("Outlet Name * (type to search all outlets)") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
        )
        if (results.isNotEmpty()) {
            Card(
                modifier  = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                results.forEach { outlet ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOutletSelected(outlet) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(outlet.outletName, Modifier.weight(1f), fontSize = 14.sp)
                        Text(
                            outlet.outletCode,
                            fontSize = 13.sp,
                            color    = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
