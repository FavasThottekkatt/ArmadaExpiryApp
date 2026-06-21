package com.armada.expiryapp.util

import android.content.Context
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.data.db.entity.Outlet
import java.security.MessageDigest

data class ItemParseResult(
    val validItems: List<Item>,
    val totalRows: Int,
    val blankBarcodes: Int,
    val blankDescriptions: Int,
    val duplicateBarcodes: Int,
    val nullProductCodes: Int,
    val skippedRows: Int,
)

data class OutletParseResult(
    val validOutlets: List<Outlet>,
    val totalRows: Int,
    val blankNames: Int,
    val duplicateCodes: Int,
    val skippedRows: Int,
)

data class CsvParseResult(
    val items: ItemParseResult,
    val outlets: OutletParseResult,
    val itemsMd5: String,
    val outletsMd5: String,
)

object CsvParser {

    // NOTE: Manual barcode/description entries are NEVER written back to CSV source files.
    // They exist only in Room database for the current month's reporting period.

    fun parse(context: Context): CsvParseResult {
        val itemBytes   = context.assets.open("ItemList.csv").use { it.readBytes() }
        val outletBytes = context.assets.open("Outlets.csv").use { it.readBytes() }
        return CsvParseResult(
            items      = parseItems(itemBytes),
            outlets    = parseOutlets(outletBytes),
            itemsMd5   = md5(itemBytes),
            outletsMd5 = md5(outletBytes),
        )
    }

    private fun toLines(bytes: ByteArray): List<String> =
        bytes.toString(Charsets.UTF_8)
            .removePrefix("﻿")   // strip UTF-8 BOM if present
            .lines()

    private fun parseItems(bytes: ByteArray): ItemParseResult {
        val rawLines          = toLines(bytes)
        val dataLines         = rawLines.drop(1).filter { it.isNotBlank() }
        var blankBarcodes     = 0
        var blankDescriptions = 0
        var duplicateBarcodes = 0
        var nullProductCodes  = 0
        var skippedRows       = 0
        val seen              = mutableSetOf<String>()
        val valid             = mutableListOf<Item>()

        for (line in dataLines) {
            val f           = parseLine(line)
            val barcode     = f.getOrElse(0) { "" }.trim()
            val description = f.getOrElse(1) { "" }.trim()
            val code        = f.getOrElse(2) { "" }.trim().takeIf { it.isNotBlank() }

            if (code == null) nullProductCodes++
            if (barcode.isBlank())     { blankBarcodes++;     skippedRows++; continue }
            if (description.isBlank()) { blankDescriptions++; skippedRows++; continue }
            if (barcode in seen)       { duplicateBarcodes++; skippedRows++; continue }

            seen += barcode
            valid += Item(barcode = barcode, description = description, productCode = code)
        }

        return ItemParseResult(
            validItems        = valid,
            totalRows         = dataLines.size,
            blankBarcodes     = blankBarcodes,
            blankDescriptions = blankDescriptions,
            duplicateBarcodes = duplicateBarcodes,
            nullProductCodes  = nullProductCodes,
            skippedRows       = skippedRows,
        )
    }

    private fun parseOutlets(bytes: ByteArray): OutletParseResult {
        val rawLines       = toLines(bytes)
        val dataLines      = rawLines.drop(1).filter { it.isNotBlank() }
        var blankNames     = 0
        var duplicateCodes = 0
        var skippedRows    = 0
        val seen           = mutableSetOf<String>()
        val valid          = mutableListOf<Outlet>()

        for (line in dataLines) {
            val f     = parseLine(line)
            val code  = f.getOrElse(0) { "" }.trim()
            val name  = f.getOrElse(1) { "" }.trim()
            val short = f.getOrElse(2) { "" }.trim()
                .ifBlank { name.take(31) }
                .take(31)   // Excel sheet tab name: max 31 chars

            if (name.isBlank()) { blankNames++;     skippedRows++; continue }
            if (code.isBlank()) {                   skippedRows++; continue }
            if (code in seen)   { duplicateCodes++; skippedRows++; continue }

            seen += code
            valid += Outlet(outletCode = code, outletName = name, shortName = short)
        }

        return OutletParseResult(
            validOutlets   = valid,
            totalRows      = dataLines.size,
            blankNames     = blankNames,
            duplicateCodes = duplicateCodes,
            skippedRows    = skippedRows,
        )
    }

    // RFC 4180-compliant parser — handles quoted fields containing commas and escaped "" quotes
    internal fun parseLine(line: String): List<String> {
        val fields   = mutableListOf<String>()
        val buf      = StringBuilder()
        var inQuotes = false
        var i        = 0

        while (i < line.length) {
            when {
                line[i] == '"' && !inQuotes -> inQuotes = true
                line[i] == '"' && inQuotes && i + 1 < line.length && line[i + 1] == '"' -> {
                    buf.append('"'); i++    // escaped ""
                }
                line[i] == '"' && inQuotes -> inQuotes = false
                line[i] == ',' && !inQuotes -> { fields += buf.toString(); buf.clear() }
                else -> buf.append(line[i])
            }
            i++
        }
        fields += buf.toString()
        return fields
    }

    private fun md5(bytes: ByteArray): String =
        MessageDigest.getInstance("MD5").digest(bytes)
            .joinToString("") { "%02x".format(it) }
}
