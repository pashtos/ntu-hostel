package com.prilki.ntuhostel.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prilki.ntuhostel.data.database.model.StudentDbModel

@Dao
interface StudentDao {
    @Query("SELECT * from students")
    suspend fun getStudents(): List<StudentDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addStudent(studentDbModel: StudentDbModel)

    @Query("DELETE FROM students WHERE id = :studentId")
    suspend fun removeStudent(studentId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM students WHERE id = :studentId LIMIT 1)")
    suspend fun existsStudent(studentId: Int): Boolean
}

