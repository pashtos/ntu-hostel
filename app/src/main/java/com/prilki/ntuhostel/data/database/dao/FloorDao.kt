package com.prilki.ntuhostel.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prilki.ntuhostel.data.database.model.FloorDbModel

@Dao
interface FloorDao {
    @Query("SELECT * from floors")
    suspend fun getFloors(): List<FloorDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFloor(floorDbModel: FloorDbModel)

    @Query("DELETE FROM floors WHERE id = :floorId")
    suspend fun removeFloor(floorId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM floors WHERE id = :floorId LIMIT 1)")
    suspend fun existsFloor(floorId: Int): Boolean
}