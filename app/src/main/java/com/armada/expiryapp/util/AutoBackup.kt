package com.armada.expiryapp.util

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AutoBackup(private val context: Context) {

    fun backup() {
        try {
            val dbFile = context.getDatabasePath("armada_expiry.db")
            if (!dbFile.exists()) return

            val backupDir = File(context.getExternalFilesDir(null), "Backups")
            backupDir.mkdirs()

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
            val backupFile = File(backupDir, "expiry_backup_$timestamp.db")

            dbFile.copyTo(backupFile, overwrite = true)
        } catch (_: Exception) {
            // Backup failure must never interrupt user flow
        }
    }
}
