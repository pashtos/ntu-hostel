package com.prilki.ntuhostel.domain

import android.app.Application
import android.util.Log
import com.prilki.ntuhostel.data.LOG_TAG
import com.prilki.ntuhostel.data.database.AppDatabase
import com.prilki.ntuhostel.data.database.mappers.FloorMapper
import com.prilki.ntuhostel.data.database.mappers.RoomMapper
import com.prilki.ntuhostel.data.database.mappers.StudentMapper
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity

class Repository private constructor(application: Application) {
    private val db = AppDatabase.getInstance(application)

    private val floorMapper = FloorMapper()
    private val studentMapper = StudentMapper()
    private val roomMapper = RoomMapper()

    private val floorDao = db.getFloorDao()
    private val studentDao = db.getStudentDao()
    private val roomDao = db.getRoomDao()

    suspend fun addStudent(student: StudentEntity): Boolean {
        if (studentDao.getStudents().any { it.name == student.name }) {
            Log.d(LOG_TAG, "student already exists")
            return false
        }
        studentDao.addStudent(studentMapper.entityToDbModel(student, student.room.id))
        return true
    }

    suspend fun addRoom(room: RoomEntity): Boolean {
        if (roomDao.getRooms().any { it.name == room.name }) {
            Log.d(LOG_TAG, "room already exists")
            return false
        }
        roomDao.addRoom(roomMapper.entityToDbModel(room, room.floor.id))
        return true
    }

    suspend fun addFloor(floor: FloorEntity): Boolean {
        if (floorDao.getFloors().any { it.number == floor.number }) {
            Log.d(LOG_TAG, "floor already exists")
            return false
        }
        floorDao.addFloor(floorMapper.entityToDbModel(floor))
        return true
    }

    suspend fun getStudentById(studentId: Int): StudentEntity {
        return getStudents().first { it.id == studentId }
    }

    suspend fun getRoomById(roomId: Int): RoomEntity {
        return getRooms().first { it.id == roomId }
    }

    suspend fun getFloorById(floorId: Int): FloorEntity {
        return getFloors().first { it.id == floorId }
    }

    suspend fun getStudents(): List<StudentEntity> {
        return studentDao.getStudents().map {
            val room = getRoomById(it.roomId)
            studentMapper.dbModelToEntity(it, room)
        }
    }

    suspend fun getRooms(): List<RoomEntity> {
        return roomDao.getRooms().map {
            val floor = getFloorById(it.floorId)
            roomMapper.dbModelToEntity(it, floor)
        }
    }

    suspend fun getFloors(): List<FloorEntity> {
        return floorDao.getFloors().map { floorMapper.dbModelToEntity(it) }
    }

    suspend fun removeFloor(floor: FloorEntity) {
        floorDao.removeFloor(floor.id)
    }

    suspend fun removeRoom(room: RoomEntity) {
        roomDao.removeRoom(room.id)
    }

    suspend fun removeStudent(student: StudentEntity) {
        studentDao.removeStudent(student.id)
    }

    companion object {
        private var instance: Repository? = null
        private val LOCK = Unit
        fun getInstance(application: Application): Repository {
            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                val newInstance = Repository(application)
                instance = newInstance
                return newInstance
            }
        }
    }
}