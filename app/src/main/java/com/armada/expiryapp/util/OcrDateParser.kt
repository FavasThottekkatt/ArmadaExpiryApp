package com.armada.expiryapp.util

import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

object OcrDateParser {

    enum class Confidence { HIGH, MEDIUM }

    enum class LabelType { EXPIRY, PRODUCTION, UNKNOWN }

    data class ParsedDate(
        val date:      LocalDate,
        val confidence: Confidence,
        val labelType:  LabelType = LabelType.UNKNOWN,
    )

    private val MONTHS_SHORT = mapOf(
        "JAN" to 1, "FEB" to 2, "MAR" to 3, "APR" to 4,
        "MAY" to 5, "JUN" to 6, "JUL" to 7, "AUG" to 8,
        "SEP" to 9, "OCT" to 10, "NOV" to 11, "DEC" to 12,
    )

    private val MONTHS_FULL = mapOf(
        "JANUARY" to 1, "FEBRUARY" to 2, "MARCH" to 3, "APRIL" to 4,
        "MAY" to 5, "JUNE" to 6, "JULY" to 7, "AUGUST" to 8,
        "SEPTEMBER" to 9, "OCTOBER" to 10, "NOVEMBER" to 11, "DECEMBER" to 12,
    )

    // Labels that indicate an expiry/best-before date — prefer these
    private val EXPIRY_LABELS = setOf(
        "EXP", "EXPIRY", "EXPIRY DATE", "BEST BEFORE", "BEST BY", "BB", "BBD",
        "USE BY", "USE BEFORE", "CONSUME BEFORE", "SELL BY", "VALID UNTIL",
        "VALID THRU", "VALID TILL", "BEST BEFORE END", "EXP DATE", "EXP:",
    )

    // Labels that indicate a manufacturing/packing date — deprioritise these
    private val PRODUCTION_LABELS = setOf(
        "MFD", "MFG", "MFGD", "MANUFACTURED", "MANUFACTURE DATE",
        "DATE OF MFG", "DATE OF MANUFACTURE", "PROD", "PRODUCTION",
        "DOM", "PKD", "PACKED", "PACKING DATE",
    )

    // Full-month names longer than 3 chars (MAY is already covered by SHORT pattern)
    private val MS = MONTHS_SHORT.keys.joinToString("|")
    private val MF = MONTHS_FULL.keys.filter { it.length > 3 }.joinToString("|")

    // Ordered most-specific first. After each match the consumed region is blanked so
    // a less-specific pattern cannot re-match a sub-string of an already-consumed date.
    private val patterns: List<Pair<Regex, (MatchResult) -> ParsedDate?>> = listOf(

        // 1. YYYY-MM-DD (ISO)
        Regex("""(\d{4})-(\d{1,2})-(\d{1,2})""") to { m ->
            tryDate(m.g(1).toInt(), m.g(2).toInt(), m.g(3).toInt(), Confidence.HIGH)
        },

        // 2. DD-MMM-YYYY  e.g. 25-JUN-2026
        Regex("""(\d{1,2})-($MS)-(\d{4})""", RegexOption.IGNORE_CASE) to { m ->
            MONTHS_SHORT[m.g(2).uppercase()]?.let { mo ->
                tryDate(m.g(3).toInt(), mo, m.g(1).toInt(), Confidence.HIGH)
            }
        },

        // 3. DD MMM YYYY  e.g. 25 JUN 2026
        Regex("""(\d{1,2})\s+($MS)\s+(\d{4})""", RegexOption.IGNORE_CASE) to { m ->
            MONTHS_SHORT[m.g(2).uppercase()]?.let { mo ->
                tryDate(m.g(3).toInt(), mo, m.g(1).toInt(), Confidence.HIGH)
            }
        },

        // 4. MMMM YYYY  e.g. JUNE 2026 / JANUARY 2026  (full names > 3 chars)
        Regex("""($MF)\s+(\d{4})""", RegexOption.IGNORE_CASE) to { m ->
            MONTHS_FULL[m.g(1).uppercase()]?.let { mo ->
                tryLastDayOf(m.g(2).toInt(), mo, Confidence.HIGH)
            }
        },

        // 5. MMM YYYY  e.g. JUN 2026 / MAY 2026
        Regex("""($MS)\s+(\d{4})""", RegexOption.IGNORE_CASE) to { m ->
            MONTHS_SHORT[m.g(1).uppercase()]?.let { mo ->
                tryLastDayOf(m.g(2).toInt(), mo, Confidence.HIGH)
            }
        },

        // 6. DD/MM/YYYY, DD-MM-YYYY, DD.MM.YYYY  (4-digit year)
        Regex("""(\d{1,2})[/\-.](\d{1,2})[/\-.](\d{4})""") to { m ->
            tryDate(m.g(3).toInt(), m.g(2).toInt(), m.g(1).toInt(), Confidence.HIGH)
        },

        // 7. MM/YYYY or MM-YYYY  (no day, 4-digit year)
        Regex("""(\d{1,2})[/\-](\d{4})""") to { m ->
            val mm = m.g(1).toInt()
            if (mm in 1..12) tryLastDayOf(m.g(2).toInt(), mm, Confidence.HIGH) else null
        },

        // 8. DD/MM/YY, DD-MM-YY, DD.MM.YY  (2-digit year)
        Regex("""(\d{1,2})[/\-.](\d{1,2})[/\-.](\d{2})""") to { m ->
            tryDate(2000 + m.g(3).toInt(), m.g(2).toInt(), m.g(1).toInt(), Confidence.MEDIUM)
        },

        // 9. MM/YY or MM-YY  (no day, 2-digit year)
        Regex("""(\d{1,2})[/\-](\d{2})""") to { m ->
            val mm = m.g(1).toInt()
            if (mm in 1..12) tryLastDayOf(2000 + m.g(2).toInt(), mm, Confidence.MEDIUM) else null
        },
    )

    /**
     * Parse all dates from [text], classify each by nearby label keywords, and return ALL of them.
     * When 2+ dates are found, the caller always shows a selection list — the parser no longer filters.
     * Sort order: expiry-labeled first, then unknown, then production.
     */
    fun parseAll(text: String): List<ParsedDate> {
        val upperText = text.uppercase(Locale.getDefault())
        val buf       = StringBuilder(upperText)

        data class RawMatch(val range: IntRange, val date: LocalDate, val confidence: Confidence)

        val rawMatches = mutableListOf<RawMatch>()
        val seen       = mutableSetOf<LocalDate>()

        for ((regex, parse) in patterns) {
            val matches = regex.findAll(buf.toString()).toList()
            for (match in matches) {
                val parsed = runCatching { parse(match) }.getOrNull() ?: continue
                for (i in match.range) { if (i < buf.length) buf[i] = ' ' }
                if (parsed.date !in seen) {
                    seen       += parsed.date
                    rawMatches += RawMatch(match.range, parsed.date, parsed.confidence)
                }
            }
        }

        if (rawMatches.isEmpty()) return emptyList()

        // Classify each date by label proximity in the original uppercased text
        val classified = rawMatches.map { raw ->
            val ctxStart  = maxOf(0, raw.range.first - 50)
            val ctxEnd    = minOf(upperText.length, raw.range.last + 51)
            val ctx       = upperText.substring(ctxStart, ctxEnd)
            val isExpiry  = EXPIRY_LABELS.any     { ctx.contains(it) }
            val isProd    = PRODUCTION_LABELS.any { ctx.contains(it) }
            val labelType = when {
                isExpiry -> LabelType.EXPIRY
                isProd   -> LabelType.PRODUCTION
                else     -> LabelType.UNKNOWN
            }
            ParsedDate(
                date       = raw.date,
                confidence = if (isExpiry) Confidence.HIGH else raw.confidence,
                labelType  = labelType,
            )
        }

        // Multiple dates → return ALL sorted: expiry first, then unknown, then production
        return if (classified.size > 1) {
            classified.sortedWith(compareBy {
                when (it.labelType) {
                    LabelType.EXPIRY     -> 0
                    LabelType.UNKNOWN    -> 1
                    LabelType.PRODUCTION -> 2
                }
            })
        } else {
            classified
        }
    }

    fun toRawDigits(date: LocalDate): String =
        "%02d%02d%04d".format(date.dayOfMonth, date.monthValue, date.year)

    fun toDisplayString(date: LocalDate): String =
        "%02d/%02d/%04d".format(date.dayOfMonth, date.monthValue, date.year)

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun MatchResult.g(index: Int) = groupValues[index]

    private fun tryDate(year: Int, month: Int, day: Int, conf: Confidence): ParsedDate? {
        if (year !in 2020..2040 || month !in 1..12 || day !in 1..31) return null
        return runCatching { ParsedDate(LocalDate.of(year, month, day), conf) }.getOrNull()
    }

    private fun tryLastDayOf(year: Int, month: Int, conf: Confidence): ParsedDate? {
        if (year !in 2020..2040 || month !in 1..12) return null
        return runCatching {
            ParsedDate(YearMonth.of(year, month).atEndOfMonth(), conf)
        }.getOrNull()
    }
}
