package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expiry_entries",
    indices = [
        Index(value = ["expiryDate"]),
        Index(value = ["archived"]),
        Index(value = ["outletCode"])
    ]
)
data class ExpiryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val description: String,
    val productCode: String?,
    val expiryDate: String,       // stored ISO: yyyy-MM-dd
    val quantity: Int,
    val unit: String = "PC",       // PC / OUT / CTN — default always PC
    val outletName: String,
    val outletCode: String,
    val merchandiser: String,
    val salesman: String,
    val entryTimestamp: String,   // stored ISO: yyyy-MM-dd HH:mm:ss
    val archived: Boolean = false
)
