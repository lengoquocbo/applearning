package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.MinitestAnswer

@Dao
interface MinitestAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: MinitestAnswer)

    @Query("SELECT * FROM minitestanswer WHERE questionID = :questionId")
    suspend fun getAnswersByQuestion(questionId: String): List<MinitestAnswer>

}