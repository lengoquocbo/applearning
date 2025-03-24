package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.ClassStudent

@Dao
interface ClassStudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassStudent(classStudent: ClassStudent)

    @Query("SELECT * FROM classstudent WHERE studentID = :studentId")
    suspend fun getClassesByStudent(studentId: String): List<String>
}