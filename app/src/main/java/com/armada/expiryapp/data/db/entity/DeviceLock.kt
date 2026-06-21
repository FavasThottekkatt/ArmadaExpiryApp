package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_lock")
data class DeviceLock(
    @PrimaryKey val id: Int = 1,
    val merchandiserName: String,
    val lockedAt: String,
)
