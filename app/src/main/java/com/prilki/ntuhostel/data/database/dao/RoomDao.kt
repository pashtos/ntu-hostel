package com.prilki.ntuhostel.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prilki.ntuhostel.data.database.model.FloorDbModel
import com.prilki.ntuhostel.data.database.model.RoomDbModel


@Dao
interface RoomDao {
    @Query("SELECT * from rooms")
    suspend fun getRooms(): List<RoomDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRoom(roomDbModel: RoomDbModel)

    @Query("DELETE FROM rooms WHERE id = :roomId")
    suspend fun removeRoom(roomId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM rooms WHERE id = :roomId LIMIT 1)")
    suspend fun existsRoom(roomId: Int): Boolean

}