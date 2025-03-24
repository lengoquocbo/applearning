package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.MinitestQuestion

@Dao
interface MinitestQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: MinitestQuestion)

    @Query("SELECT * FROM minitestquestion WHERE minitestsID = :minitestId")
    suspend fun getQuestionsByMinitest(minitestId: String): List<MinitestQuestion>
}