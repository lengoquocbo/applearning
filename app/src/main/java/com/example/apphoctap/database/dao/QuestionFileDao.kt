package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.QuestionFileEntity

@Dao
interface QuestionFileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionFile(file: QuestionFileEntity): Long

    @Update
    suspend fun updateQuestionFile(file: QuestionFileEntity)

    @Query("SELECT * FROM question_file ORDER BY lastModified DESC")
    suspend fun getAllQuestionFiles(): List<QuestionFileEntity>

    @Query("SELECT * FROM question_file WHERE fileLocalId = :fileLocalId")
    suspend fun getQuestionFileById(fileLocalId: Long): QuestionFileEntity?

    @Query("SELECT * FROM question_file WHERE isSynced = 0")
    suspend fun getUnsyncedQuestionFiles(): List<QuestionFileEntity>

    @Query("UPDATE question_file SET totalQuestions = :totalQuestions WHERE fileLocalId = :fileLocalId")
    suspend fun updateQuestionCount(fileLocalId: Long, totalQuestions: Int)

    @Query("UPDATE question_file SET isDownloaded = :isDownloaded, localFilePath = :localFilePath WHERE fileLocalId = :fileLocalId")
    suspend fun updateDownloadStatus(fileLocalId: Long, isDownloaded: Boolean, localFilePath: String?)

    @Delete
    suspend fun deleteQuestionFile(file: QuestionFileEntity)
}