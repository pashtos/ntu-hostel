package com.prilki.ntuhostel.data.database.model

import androidx.room.*

@Entity(
    tableName = "students",
    indices = [Index(value = ["name", "group"], unique = true)],
    foreignKeys = [ForeignKey(
        RoomDbModel::class,
        parentColumns = ["id"],
        childColumns = ["room_id"],
        onDelete = ForeignKey.RESTRICT
    )]
)

data class StudentDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val group: String,
    @ColumnInfo(name = "room_id") val roomId: Int
)
