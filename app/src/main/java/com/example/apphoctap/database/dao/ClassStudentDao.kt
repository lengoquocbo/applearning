package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.apphoctap.model.ClassStudent

@Dao
interface ClassStudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassStudent(classStudent: ClassStudent)

}