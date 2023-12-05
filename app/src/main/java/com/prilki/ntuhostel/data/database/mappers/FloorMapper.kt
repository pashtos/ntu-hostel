package com.prilki.ntuhostel.data.database.mappers

import com.prilki.ntuhostel.data.database.model.FloorDbModel
import com.prilki.ntuhostel.domain.entities.FloorEntity

class FloorMapper {

    fun dbModelToEntity(floorDbModel: FloorDbModel): FloorEntity {
        return FloorEntity(floorDbModel.id, floorDbModel.number)
    }

    fun entityToDbModel(floor: FloorEntity): FloorDbModel {
        return FloorDbModel(floor.id, floor.number)
    }
}