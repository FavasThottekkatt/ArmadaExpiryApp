package com.armada.expiryapp.util

import android.content.Context
import android.os.Environment
import android.util.Log
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Outlet
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ExcelExporter(private val context: Context) {

    private val excelDateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    companion object {
        private const val TAG = "ExcelExporter"
    }

    @Throws(Exception::class, OutOfMemoryError::class)
    fun buildAndSaveFile(
        entries:      List<ExpiryEntry>,
        outlet:       Outlet,
        merchandiser: String,
        salesman:     String,
        fileLabel:    String = "Expiry Report",
    ): File {
        val monthYear = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))

        val sanitize = { s: String -> s.replace(Regex("[\\\\/:*?\"<>|]"), "").trim() }

        val fileName = "${sanitize(merchandiser)} – ${sanitize(salesman)} – $monthYear – ${sanitize(fileLabel)}.xlsx"
        val docsDir  = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?: throw IllegalStateException("External Documents folder unavailable")
        docsDir.mkdirs()
        val file = File(docsDir, fileName)

        val sheetName = outlet.shortName.take(31)

        var workbook: XSSFWorkbook? = null
        try {
            workbook = if (file.exists()) {
                file.inputStream().use { XSSFWorkbook(it) }
            } else {
                XSSFWorkbook()
            }

            val sheetIndex = workbook.getSheetIndex(sheetName)
            val sheet: XSSFSheet = if (sheetIndex >= 0) {
                val s = workbook.getSheetAt(sheetIndex)
                for (r in s.lastRowNum downTo 0) s.getRow(r)?.let { s.removeRow(it) }
                s
            } else {
                workbook.createSheet(sheetName)
            }

            fun cell(rowIdx: Int, colIdx: Int, value: String) {
                val row = sheet.getRow(rowIdx) ?: sheet.createRow(rowIdx)
                (row.getCell(colIdx) ?: row.createCell(colIdx)).setCellValue(value)
            }

            // Rows 0–3: metadata; Row 4: blank; Row 5: column headers; Row 6+: data
            cell(0, 0, "SALESMAN :")      ; cell(0, 1, salesman)
            cell(1, 0, "MERCHANDISER :") ; cell(1, 1, merchandiser)
            cell(2, 0, "OUTLET NAME :")  ; cell(2, 1, outlet.outletName)
            cell(3, 0, "CODE :")          ; cell(3, 1, outlet.outletCode)
            cell(5, 0, "CODE")
            cell(5, 1, "DESCRIPTION")
            cell(5, 2, "EXPIRY DATE")
            cell(5, 3, "QTY")

            entries.forEachIndexed { i, e ->
                val rowNum = 6 + i
                cell(rowNum, 0, e.productCode ?: "")
                cell(rowNum, 1, e.description)
                cell(rowNum, 2, runCatching {
                    LocalDate.parse(e.expiryDate).format(excelDateFmt)
                }.getOrDefault(e.expiryDate))
                cell(rowNum, 3, "${e.quantity} ${e.unit}")
            }

            // Fixed column widths (1/256th char unit): autoSizeColumn requires AWT, unavailable on Android
            sheet.setColumnWidth(0, 3840)   // CODE       ~15 chars
            sheet.setColumnWidth(1, 11520)  // DESCRIPTION ~45 chars
            sheet.setColumnWidth(2, 3584)   // EXPIRY DATE ~14 chars
            sheet.setColumnWidth(3, 3072)   // QTY         ~12 chars

            Log.d(TAG, "Writing workbook to ${file.absolutePath} (${entries.size} rows)")
            try {
                file.outputStream().use { workbook.write(it) }
                Log.i(TAG, "Excel export saved: ${file.name} (${file.length()} bytes)")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to write workbook: ${e.javaClass.simpleName} — ${e.message}", e)
                throw e
            }
        } finally {
            workbook?.close()
        }

        return file
    }

    @Throws(Exception::class, OutOfMemoryError::class)
    fun buildMultiOutletFile(
        outletEntries: List<Triple<Outlet, String, List<ExpiryEntry>>>,
        merchandiser:  String,
    ): File {
        val monthYear = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
        val sanitize = { s: String -> s.replace(Regex("[\\\\/:*?\"<>|]"), "").trim() }
        val fileName = "${sanitize(merchandiser)} – $monthYear – Expiry Report.xlsx"
        val docsDir  = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?: throw IllegalStateException("External Documents folder unavailable")
        docsDir.mkdirs()
        val file = File(docsDir, fileName)

        var workbook: XSSFWorkbook? = null
        try {
            workbook = if (file.exists()) {
                runCatching { file.inputStream().use { XSSFWorkbook(it) } }.getOrElse { XSSFWorkbook() }
            } else {
                XSSFWorkbook()
            }

            outletEntries.forEach { (outlet, salesmanName, entries) ->
                val sheetName = outlet.shortName.ifBlank { outlet.outletName }.take(31)

                val existingIdx = workbook.getSheetIndex(sheetName)
                if (existingIdx >= 0) workbook.removeSheetAt(existingIdx)
                val sheet: XSSFSheet = workbook.createSheet(sheetName)

                writeCell(sheet.createRow(0), 0, "SALESMAN :")    ; writeCell(sheet.getRow(0), 1, salesmanName)
                writeCell(sheet.createRow(1), 0, "MERCHANDISER :"); writeCell(sheet.getRow(1), 1, merchandiser)
                writeCell(sheet.createRow(2), 0, "OUTLET NAME :") ; writeCell(sheet.getRow(2), 1, outlet.outletName)
                writeCell(sheet.createRow(3), 0, "CODE :")         ; writeCell(sheet.getRow(3), 1, outlet.outletCode)
                sheet.createRow(4)
                sheet.createRow(5).also { row ->
                    writeCell(row, 0, "CODE")
                    writeCell(row, 1, "DESCRIPTION")
                    writeCell(row, 2, "EXPIRY DATE")
                    writeCell(row, 3, "QTY")
                }

                entries.sortedBy { it.expiryDate }.forEachIndexed { i, e ->
                    sheet.createRow(6 + i).also { row ->
                        writeCell(row, 0, e.productCode ?: "")
                        writeCell(row, 1, e.description)
                        writeCell(row, 2, runCatching {
                            LocalDate.parse(e.expiryDate).format(excelDateFmt)
                        }.getOrDefault(e.expiryDate))
                        writeCell(row, 3, "${e.quantity} ${e.unit}")
                    }
                }

                sheet.setColumnWidth(0, 3840)
                sheet.setColumnWidth(1, 11520)
                sheet.setColumnWidth(2, 3584)
                sheet.setColumnWidth(3, 3072)
            }

            file.outputStream().use { workbook.write(it) }
            Log.i(TAG, "Multi-outlet Excel saved: ${file.name}")
            return file
        } finally {
            workbook?.close()
        }
    }

    private fun writeCell(row: XSSFRow, col: Int, value: String) {
        row.createCell(col).setCellValue(value)
    }
}
