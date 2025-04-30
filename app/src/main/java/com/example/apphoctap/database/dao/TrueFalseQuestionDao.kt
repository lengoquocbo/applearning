package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.TrueFalseQuestionEntity

@Dao
interface TrueFalseQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: TrueFalseQuestionEntity): Long

    @Update
    suspend fun updateQuestion(question: TrueFalseQuestionEntity)

    @Query("SELECT * FROM true_false_question WHERE fileLocalId = :fileLocalId ORDER BY orderInFile")
    suspend fun getQuestionsByFile(fileLocalId: Long): List<TrueFalseQuestionEntity>

    @Query("SELECT COUNT(*) FROM true_false_question WHERE fileLocalId = :fileLocalId")
    suspend fun getQuestionCountByFile(fileLocalId: Long): Int

    @Query("SELECT * FROM true_false_question WHERE isSynced = 0")
    suspend fun getUnsyncedQuestions(): List<TrueFalseQuestionEntity>

    @Delete
    suspend fun deleteQuestion(question: TrueFalseQuestionEntity)

    @Query("DELETE FROM true_false_question WHERE fileLocalId = :fileLocalId")
    suspend fun deleteQuestionsByFile(fileLocalId: Long)
}