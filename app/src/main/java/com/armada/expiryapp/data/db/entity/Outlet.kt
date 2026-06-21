package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "outlets",
    indices = [Index(value = ["outletName"])]
)
data class Outlet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val outletCode: String,
    val outletName: String,
    val shortName: String
)
