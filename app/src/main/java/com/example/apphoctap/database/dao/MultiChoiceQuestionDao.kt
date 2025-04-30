package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.MultiChoiceQuestionEntity

@Dao
interface MultiChoiceQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: MultiChoiceQuestionEntity): Long

    @Update
    suspend fun updateQuestion(question: MultiChoiceQuestionEntity)

    @Query("SELECT * FROM multi_choice_question WHERE fileLocalId = :fileLocalId ORDER BY orderInFile")
    suspend fun getQuestionsByFile(fileLocalId: Long): List<MultiChoiceQuestionEntity>

    @Query("SELECT COUNT(*) FROM multi_choice_question WHERE fileLocalId = :fileLocalId")
    suspend fun getQuestionCountByFile(fileLocalId: Long): Int

    @Query("SELECT * FROM multi_choice_question WHERE isSynced = 0")
    suspend fun getUnsyncedQuestions(): List<MultiChoiceQuestionEntity>

    @Delete
    suspend fun deleteQuestion(question: MultiChoiceQuestionEntity)

    @Query("DELETE FROM multi_choice_question WHERE fileLocalId = :fileLocalId")
    suspend fun deleteQuestionsByFile(fileLocalId: Long)
}