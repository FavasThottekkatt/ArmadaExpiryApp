package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "outlet_item_links",
    indices = [
        Index(value = ["outletCode"]),
        Index(value = ["barcode"]),
    ],
)
data class OutletItemLink(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val outletCode:  String,
    val barcode:     String,
    val description: String,
    val productCode: String?,
)
