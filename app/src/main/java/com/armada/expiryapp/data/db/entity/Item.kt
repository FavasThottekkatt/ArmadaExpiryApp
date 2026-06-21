package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    indices = [
        Index(value = ["barcode"], unique = true),
        Index(value = ["description"]),
        Index(value = ["productCode"])
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val description: String,
    val productCode: String?
)
