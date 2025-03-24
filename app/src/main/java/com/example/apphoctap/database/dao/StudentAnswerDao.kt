package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.StudentAnswer

@Dao
interface StudentAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentAnswer(studentAnswer: StudentAnswer)

    @Query("SELECT * FROM studentanswer WHERE resultID = :resultId")
    suspend fun getStudentAnswersByResult(resultId: String): List<StudentAnswer>
}