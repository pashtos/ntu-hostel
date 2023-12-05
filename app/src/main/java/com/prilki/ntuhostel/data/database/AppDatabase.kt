package com.prilki.ntuhostel.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prilki.ntuhostel.data.DB_NAME
import com.prilki.ntuhostel.data.database.dao.FloorDao
import com.prilki.ntuhostel.data.database.dao.RoomDao
import com.prilki.ntuhostel.data.database.dao.StudentDao
import com.prilki.ntuhostel.data.database.model.FloorDbModel
import com.prilki.ntuhostel.data.database.model.RoomDbModel
import com.prilki.ntuhostel.data.database.model.StudentDbModel

@Database(
    entities = [FloorDbModel::class, RoomDbModel::class, StudentDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFloorDao(): FloorDao
    abstract fun getStudentDao(): StudentDao
    abstract fun getRoomDao(): RoomDao

    companion object {
        private var instance: AppDatabase? = null
        private val LOCK = Unit
        fun getInstance(application: Application): AppDatabase {
            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                val newInstance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = newInstance
                return newInstance
            }
        }
    }
}