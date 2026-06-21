package com.armada.expiryapp.ui.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.armada.expiryapp.ui.theme.ArmadaColors
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun CalendarDatePicker(
    currentRaw:     String,
    onDateSelected: (String) -> Unit,
    onDismiss:      () -> Unit,
) {
    val today     = remember { LocalDate.now() }
    val headerFmt = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) }

    val selectedDate: LocalDate? = remember(currentRaw) {
        if (currentRaw.length == 8) runCatching {
            LocalDate.of(
                currentRaw.drop(4).toInt(),
                currentRaw.substring(2, 4).toInt(),
                currentRaw.take(2).toInt(),
            )
        }.getOrNull() else null
    }

    var displayedYM  by remember {
        mutableStateOf(selectedDate?.let { YearMonth.from(it) } ?: YearMonth.now())
    }
    var pendingExpired by remember { mutableStateOf<LocalDate?>(null) }

    // ── Expired-date confirmation ─────────────────────────────────────────────
    pendingExpired?.let { expired ->
        AlertDialog(
            onDismissRequest = { pendingExpired = null },
            title   = { Text("Past Expiry Date") },
            text    = {
                Text(
                    "The selected date (${expired.toDisplayStr()}) has already passed.\n" +
                    "Are you sure you want to record this?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(expired.toRawDigits())
                    pendingExpired = null
                    onDismiss()
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { pendingExpired = null }) { Text("Cancel") }
            },
        )
    }

    // ── Calendar dialog ───────────────────────────────────────────────────────
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = ArmadaColors.BgCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Month / year header
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { displayedYM = displayedYM.minusMonths(1) }) {
                        Icon(
                            Icons.Filled.ChevronLeft,
                            contentDescription = "Previous month",
                            tint               = ArmadaColors.BrandTitle,
                        )
                    }
                    Text(
                        text       = displayedYM.format(headerFmt),
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = ArmadaColors.BrandTitle,
                        textAlign  = TextAlign.Center,
                        modifier   = Modifier.weight(1f),
                    )
                    IconButton(onClick = { displayedYM = displayedYM.plusMonths(1) }) {
                        Icon(
                            Icons.Filled.ChevronRight,
                            contentDescription = "Next month",
                            tint               = ArmadaColors.BrandTitle,
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Day-of-week labels (Sunday first)
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { label ->
                        Text(
                            text       = label,
                            modifier   = Modifier.weight(1f),
                            textAlign  = TextAlign.Center,
                            style      = MaterialTheme.typography.labelSmall,
                            color      = ArmadaColors.TextSecondary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Day grid
                val startOffset = displayedYM.atDay(1).dayOfWeek.value % 7  // 0 = Sunday
                val daysInMonth = displayedYM.lengthOfMonth()
                val numRows     = (startOffset + daysInMonth + 6) / 7

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(numRows) { row ->
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            repeat(7) { col ->
                                val dayNum = row * 7 + col - startOffset + 1
                                if (dayNum < 1 || dayNum > daysInMonth) {
                                    Box(Modifier.weight(1f).aspectRatio(1f))
                                } else {
                                    val date      = displayedYM.atDay(dayNum)
                                    val isSelected = date == selectedDate
                                    val isToday    = date == today
                                    val daysUntil  = ChronoUnit.DAYS.between(today, date)

                                    val cellBg = when {
                                        isSelected    -> ArmadaColors.BrandAccent
                                        daysUntil < 0  -> ArmadaColors.CalExpired
                                        daysUntil <= 30 -> ArmadaColors.CalSoon
                                        else           -> ArmadaColors.CalSafe
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clip(CircleShape)
                                            .background(cellBg)
                                            .then(
                                                if (isToday && !isSelected)
                                                    Modifier.border(1.5.dp, ArmadaColors.BrandAccent, CircleShape)
                                                else Modifier
                                            )
                                            .clickable {
                                                if (daysUntil < 0) {
                                                    pendingExpired = date
                                                } else {
                                                    onDateSelected(date.toRawDigits())
                                                    onDismiss()
                                                }
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text       = dayNum.toString(),
                                            style      = MaterialTheme.typography.bodySmall,
                                            color      = if (isSelected) Color.White
                                                         else ArmadaColors.TextPrimary,
                                            fontWeight = if (isToday || isSelected) FontWeight.Bold
                                                         else FontWeight.Normal,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = ArmadaColors.Border, thickness = 0.5.dp)
                Spacer(Modifier.height(8.dp))

                // Color legend
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    CalLegendItem(ArmadaColors.CalExpired, "Expired")
                    CalLegendItem(ArmadaColors.CalSoon, "≤30 days")
                    CalLegendItem(ArmadaColors.CalSafe, "Safe")
                }

                // Cancel
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = ArmadaColors.TextSecondary)
                    }
                }
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

@Composable
private fun CalLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall,
            color = ArmadaColors.TextSecondary,
        )
    }
}

private fun LocalDate.toRawDigits(): String =
    "%02d%02d%04d".format(dayOfMonth, monthValue, year)

private fun LocalDate.toDisplayStr(): String =
    "%02d/%02d/%04d".format(dayOfMonth, monthValue, year)
