package com.prilki.ntuhostel.data.database.mappers

import com.prilki.ntuhostel.data.database.model.RoomDbModel
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.domain.entities.RoomEntity

class RoomMapper {

    fun dbModelToEntity(roomDbModel: RoomDbModel, floor: FloorEntity): RoomEntity {
        return RoomEntity(
            id = roomDbModel.id,
            name = roomDbModel.name,
            capacity = roomDbModel.capacity,
            floor = floor
        )
    }

    fun entityToDbModel(room: RoomEntity, floorId: Int): RoomDbModel {
        return RoomDbModel(
            id = room.id,
            name = room.name,
            capacity = room.capacity,
            floorId = floorId
        )
    }
}