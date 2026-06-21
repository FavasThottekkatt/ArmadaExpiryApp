package com.armada.expiryapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val ArmadaColorScheme = lightColorScheme(
    primary             = ArmadaColors.BrandAccent,
    onPrimary           = ArmadaColors.TextOnDark,
    primaryContainer    = ArmadaColors.FieldActiveBg,
    onPrimaryContainer  = ArmadaColors.BrandTitle,
    secondary           = ArmadaColors.BrandTitle,
    onSecondary         = ArmadaColors.TextOnDark,
    secondaryContainer  = ArmadaColors.FieldFilledBg,
    onSecondaryContainer= ArmadaColors.TextPrimary,
    background          = ArmadaColors.BgApp,
    onBackground        = ArmadaColors.TextPrimary,
    surface             = ArmadaColors.BgCard,
    onSurface           = ArmadaColors.TextPrimary,
    surfaceVariant      = ArmadaColors.NumpadBg,
    onSurfaceVariant    = ArmadaColors.TextSecondary,
    outline             = ArmadaColors.Border,
    error               = ArmadaColors.StatusExpired,
    onError             = ArmadaColors.TextOnDark,
)

// Section 5 typography scale — Roboto (system default)
val ArmadaTypography = Typography(
    // Screen titles: 24sp bold
    titleLarge  = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 24.sp),
    // Section headers: 18sp medium
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
    // Body text: 14sp regular
    bodyLarge   = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    // Table cells: 13sp
    bodyMedium  = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp),
    // Hints / placeholders: 12sp
    labelSmall  = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp),
    // App bar subtitle label
    labelMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp),
)

@Composable
fun ArmadaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArmadaColorScheme,
        typography  = ArmadaTypography,
        content     = content,
    )
}
