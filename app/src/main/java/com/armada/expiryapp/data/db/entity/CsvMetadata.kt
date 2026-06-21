package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "csv_metadata")
data class CsvMetadata(
    @PrimaryKey val fileType: String,   // "ITEMS" or "OUTLETS"
    val importedAt: String,             // ISO timestamp: yyyy-MM-dd HH:mm:ss
    val recordCount: Int,
    val skippedRows: Int,
    val fileHash: String                // MD5 of CSV for change detection
)
