package com.armada.expiryapp.ui.theme

import androidx.compose.ui.graphics.Color

object ArmadaColors {
    // ── Backgrounds ─────────────────────────────────────────────────────────
    val BgApp              = Color(0xFFF0F4F8)
    val BgCard             = Color(0xFFFFFFFF)
    val BgHeader           = Color(0xFF1E3A5F)

    // ── Brand ───────────────────────────────────────────────────────────────
    val BrandAccent        = Color(0xFF2E86C1)
    val BrandTitle         = Color(0xFF1A5276)
    val TableHeader        = Color(0xFF2874A6)

    // ── Navigation ──────────────────────────────────────────────────────────
    val NavActive          = Color(0xFF2E86C1)
    val NavInactive        = Color(0xFF95A5A6)

    // ── Text ────────────────────────────────────────────────────────────────
    val TextPrimary        = Color(0xFF1C2833)
    val TextSecondary      = Color(0xFF5D6D7E)
    val TextOnDark         = Color(0xFFFFFFFF)

    // ── Borders & shadows ───────────────────────────────────────────────────
    val Border             = Color(0xFFD5D8DC)
    val Shadow             = Color(0x14000000)

    // ── Input field states ──────────────────────────────────────────────────
    val FieldActiveBg      = Color(0xFFE8F5E9)
    val FieldActiveBorder  = Color(0xFF27AE60)
    val FieldActiveLabel   = Color(0xFF1E8449)
    val FieldFilledBg      = Color(0xFFEAF4FB)
    val FieldManualBg      = Color(0xFFFFF3F3)
    val FieldManualBorder  = Color(0xFFE74C3C)

    // ── Numpad ──────────────────────────────────────────────────────────────
    val NumpadBg           = Color(0xFFECEFF1)
    val NumpadKey          = Color(0xFFFFFFFF)
    val NumpadKeyBorder    = Color(0xFFCFD8DC)
    val NumpadKeyText      = Color(0xFF1C2833)
    val NumpadClear        = Color(0xFFFFEBEE)
    val NumpadClearText    = Color(0xFFC0392B)

    // ── Expiry status ───────────────────────────────────────────────────────
    val StatusExpired      = Color(0xFFC0392B)
    val StatusD30          = Color(0xFFE67E22)
    val StatusD60          = Color(0xFFD4AC0D)
    val StatusD90          = Color(0xFF27AE60)
    val StatusSafe         = Color(0xFF1E8449)

    // ── Date picker color coding ─────────────────────────────────────────────
    val CalExpired         = Color(0xFFFFCDD2)
    val CalSoon            = Color(0xFFFFE0B2)
    val CalSafe            = Color(0xFFC8E6C9)

    // ── Buttons ─────────────────────────────────────────────────────────────
    val BtnLogin           = Color(0xFF1E3A5F)
    val BtnOcr             = Color(0xFF1A5276)
    val BtnNext            = Color(0xFF2E86C1)
    val BtnNewEntryReady   = Color(0xFF1E8449)
    val BtnNewEntryInactive= Color(0xFFBDC3C7)

    // ── Disabled states ──────────────────────────────────────────────────────
    val Disabled           = Color(0xFFBDC3C7)
    val DisabledText       = Color(0xFF7F8C8D)

    // ── FAB ─────────────────────────────────────────────────────────────────
    val FabBg              = Color(0xFF2E86C1)
    val FabIcon            = Color(0xFFFFFFFF)

    // ── Snackbar ─────────────────────────────────────────────────────────────
    val SnackbarUndo       = Color(0xFF27AE60)

    // ── [V4] Bulk / Repeat mode ──────────────────────────────────────────────
    val BulkModeBg         = Color(0xFFFFF8E1)
    val BulkModeBorder     = Color(0xFFF39C12)

    // ── [V4] Startup health check ────────────────────────────────────────────
    val HealthOk           = Color(0xFF27AE60)
    val HealthWarn         = Color(0xFFE67E22)
    val HealthFail         = Color(0xFFC0392B)

    // ── [V4] CSV validation screen ───────────────────────────────────────────
    val CsvValidBg         = Color(0xFFE8F5E9)
    val CsvWarnBg          = Color(0xFFFFF3E0)

    // ── [V4] Stock screen ─────────────────────────────────────────────────────
    val StockOosActive     = Color(0xFFC0392B)  // OOS LED — red
    val StockOosInactive   = Color(0xFFBDC3C7)  // OOS LED — grey
    val StockOosRowBg      = Color(0xFFFFEBEE)  // Row tint when OOS
    val StockQtyRowBg      = Color(0xFFFFFDE7)  // Row tint when qty entered
    val StockQtyBg         = Color(0xFFFFF176)  // QTY cell background when value entered
    val StockQtyBorder     = Color(0xFFF9A825)  // QTY cell border when value entered
}
