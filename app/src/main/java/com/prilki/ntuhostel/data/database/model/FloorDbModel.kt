package com.prilki.ntuhostel.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "floors",
    indices = [Index(value = ["number"], unique = true)]
)
data class FloorDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val number: Int,
)
