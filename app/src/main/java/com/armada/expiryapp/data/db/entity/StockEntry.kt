package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stock_entries",
    indices = [
        Index(value = ["outletCode"]),
        Index(value = ["archived"]),
    ]
)
data class StockEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val description: String,
    val productCode: String?,
    val isOos: Boolean = false,
    val quantity: Int = 0,
    val unit: String = "PC",
    val outletName: String,
    val outletCode: String,
    val merchandiser: String,
    val salesman: String,
    val entryTimestamp: String,
    val archived: Boolean = false,
)
