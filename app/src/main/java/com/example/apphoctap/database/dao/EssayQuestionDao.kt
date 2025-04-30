package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.EssayQuestionEntity

@Dao
interface EssayQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: EssayQuestionEntity): Long

    @Update
    suspend fun updateQuestion(question: EssayQuestionEntity)

    @Query("SELECT * FROM essay_question WHERE fileLocalId = :fileLocalId ORDER BY orderInFile")
    suspend fun getQuestionsByFile(fileLocalId: Long): List<EssayQuestionEntity>

    @Query("SELECT COUNT(*) FROM essay_question WHERE fileLocalId = :fileLocalId")
    suspend fun getQuestionCountByFile(fileLocalId: Long): Int

    @Query("SELECT * FROM essay_question WHERE isSynced = 0")
    suspend fun getUnsyncedQuestions(): List<EssayQuestionEntity>

    @Delete
    suspend fun deleteQuestion(question: EssayQuestionEntity)

    @Query("DELETE FROM essay_question WHERE fileLocalId = :fileLocalId")
    suspend fun deleteQuestionsByFile(fileLocalId: Long)
}