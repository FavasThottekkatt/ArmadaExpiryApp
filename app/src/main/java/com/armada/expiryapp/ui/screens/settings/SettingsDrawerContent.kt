package com.armada.expiryapp.ui.screens.settings

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.armada.expiryapp.ui.theme.ArmadaColors
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsDrawerContent(
    onReimport:    () -> Unit,
    onItemLinking: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context          = LocalContext.current
    val backupFiles      by viewModel.backupFiles.collectAsState()
    val crashLogContent  by viewModel.crashLogContent.collectAsState()
    val linkedItemCount  by viewModel.linkedItemCount.collectAsState()
    val snackState       = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.refreshLinkedCount()
        viewModel.snackMessage.collect { msg ->
            snackState.showSnackbar(msg)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.shareRequest.collect { req ->
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                req.file,
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type    = req.mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share ${req.file.name}"))
        }
    }

    // Crash log dialog
    if (crashLogContent != null) {
        AlertDialog(
            onDismissRequest = viewModel::dismissCrashLog,
            title = { Text("Crash Log") },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        text       = crashLogContent ?: "",
                        style      = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        color      = ArmadaColors.TextPrimary,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::dismissCrashLog) { Text("Close") }
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 64.dp),
        ) {
            // ── Header ──────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ArmadaColors.BgHeader)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            ) {
                Text(
                    text       = "SETTINGS",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = ArmadaColors.TextOnDark,
                )
                Text(
                    text  = "Armada Distribution",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArmadaColors.TextOnDark.copy(alpha = 0.7f),
                )
            }

            // ── Re-import ─────────────────────────────────────────────────────
            OutlinedButton(
                onClick  = onReimport,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text("Re-import Master Data")
            }

            SectionDivider()

            // ── Item Linking ───────────────────────────────────────────────────
            SectionHeader(icon = Icons.Filled.Link, title = "ITEM LINKING")

            val hasOutlet = viewModel.hasOutlet
            OutlinedButton(
                onClick  = { if (hasOutlet) onItemLinking() },
                enabled  = hasOutlet,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Text(
                    if (hasOutlet) "Item Linking — $linkedItemCount items linked"
                    else "Item Linking — no outlet selected"
                )
            }

            SectionDivider()

            // ── Backups ──────────────────────────────────────────────────────
            SectionHeader(icon = Icons.Filled.Backup, title = "BACKUPS")

            if (backupFiles.isEmpty()) {
                Text(
                    text     = "No backups yet.",
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = ArmadaColors.TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            } else {
                backupFiles.take(10).forEach { file ->
                    BackupFileRow(
                        file    = file,
                        onShare = { viewModel.shareBackup(file) },
                    )
                }
            }

            SectionDivider()

            // ── Crash Log ────────────────────────────────────────────────────
            SectionHeader(icon = Icons.Filled.BugReport, title = "CRASH LOG")

            val crashExists = viewModel.crashLogFile.exists()
            if (!crashExists) {
                Text(
                    text     = "No crashes recorded.",
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = ArmadaColors.TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                ) {
                    OutlinedButton(onClick = {
                        viewModel.loadCrashLog()
                    }) { Text("View") }

                    OutlinedButton(onClick = viewModel::shareCrashLog) { Text("Share") }

                    TextButton(onClick = viewModel::clearCrashLog) {
                        Text("Clear", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            SectionDivider()

            // ── About ────────────────────────────────────────────────────────
            SectionHeader(icon = Icons.Filled.Info, title = "ABOUT")

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text  = "Version ${viewModel.appVersion} (${viewModel.versionCode})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArmadaColors.TextPrimary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text  = "Armada Distribution",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArmadaColors.TextSecondary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text  = "Expiry Tracking App",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArmadaColors.TextSecondary,
                )
            }

            Spacer(Modifier.height(16.dp))
        }

        SnackbarHost(
            hostState = snackState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment  = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp),
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = ArmadaColors.BrandAccent,
            modifier           = Modifier.size(16.dp),
        )
        Text(
            text       = title,
            style      = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color      = ArmadaColors.BrandAccent,
        )
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier  = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color     = ArmadaColors.Border,
    )
}

@Composable
private fun BackupFileRow(file: File, onShare: () -> Unit) {
    val dateLabel = remember(file) {
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(file.lastModified()))
    }
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = file.name,
                style = MaterialTheme.typography.bodySmall,
                color = ArmadaColors.TextPrimary,
            )
            Text(
                text  = dateLabel,
                style = MaterialTheme.typography.labelSmall,
                color = ArmadaColors.TextSecondary,
            )
        }
        TextButton(onClick = onShare) {
            Text("Share", color = ArmadaColors.BrandAccent)
        }
    }
}
