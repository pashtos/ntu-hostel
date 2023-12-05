package com.prilki.ntuhostel.data.database.mappers

import com.prilki.ntuhostel.data.database.model.StudentDbModel
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity

class StudentMapper {

    fun dbModelToEntity(studentDbModel: StudentDbModel, room: RoomEntity): StudentEntity {
        return StudentEntity(
            id = studentDbModel.id,
            name = studentDbModel.name,
            group = studentDbModel.group,
            room = room
        )
    }

    fun entityToDbModel(student: StudentEntity, roomId: Int): StudentDbModel {
        return StudentDbModel(
            id = student.id,
            name = student.name,
            group = student.group,
            roomId = roomId
        )
    }
}