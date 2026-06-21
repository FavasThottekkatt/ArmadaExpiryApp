package com.armada.expiryapp.util

import androidx.compose.ui.graphics.Color
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.ui.theme.ArmadaColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class Status { EXPIRED, WITHIN_30, WITHIN_60, WITHIN_90, SAFE }

// Accept a pre-computed today so callers don't allocate LocalDate.now() per item.
fun ExpiryEntry.status(today: LocalDate = LocalDate.now()): Status {
    return try {
        val expiry = LocalDate.parse(expiryDate)
        val days   = ChronoUnit.DAYS.between(today, expiry)
        when {
            days < 0   -> Status.EXPIRED
            days <= 30 -> Status.WITHIN_30
            days <= 60 -> Status.WITHIN_60
            days <= 90 -> Status.WITHIN_90
            else       -> Status.SAFE
        }
    } catch (e: Exception) {
        Status.SAFE
    }
}

fun Status.dotColor(): Color = when (this) {
    Status.EXPIRED   -> ArmadaColors.StatusExpired
    Status.WITHIN_30 -> ArmadaColors.StatusD30
    Status.WITHIN_60 -> ArmadaColors.StatusD60
    Status.WITHIN_90 -> ArmadaColors.StatusD90
    Status.SAFE      -> ArmadaColors.StatusSafe
}

private val displayDateFmt = DateTimeFormatter.ofPattern("dd/MM/yy")

fun String.toDisplayDate(): String = try {
    LocalDate.parse(this).format(displayDateFmt)
} catch (e: Exception) { this }
