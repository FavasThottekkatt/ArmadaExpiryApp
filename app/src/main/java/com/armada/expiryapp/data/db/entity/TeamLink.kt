package com.armada.expiryapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "team_links",
    indices = [Index(value = ["outletCode"])],
)
data class TeamLink(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val merchandiserName: String,
    val salesmanName: String,
    val outletCode: String,
    val outletName: String,
)
