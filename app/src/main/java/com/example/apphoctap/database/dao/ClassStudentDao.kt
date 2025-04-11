package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.ClassStudent
import com.example.apphoctap.model.Classes

@Dao
interface ClassStudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassStudent(classStudent: ClassStudent)

    @Query("SELECT * FROM classstudent WHERE studentID = :studentID")
    suspend fun getClassStudentByStudent(studentID: String): List<ClassStudent>
}