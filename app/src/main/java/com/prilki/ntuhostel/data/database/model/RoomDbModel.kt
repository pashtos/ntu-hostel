package com.prilki.ntuhostel.data.database.model

import androidx.room.*

@Entity(
    tableName = "rooms",
    indices = [Index(value = ["name"], unique = true)],
    foreignKeys = [ForeignKey(
        FloorDbModel::class,
        parentColumns = ["id"],
        childColumns = ["floor_id"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class RoomDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val capacity: Int,
    @ColumnInfo(name = "floor_id") val floorId: Int
)
