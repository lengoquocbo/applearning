package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.Flashcard

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM flashcards WHERE assignmentID = :assignmentId")
    suspend fun getFlashcardsByAssignment(assignmentId: String): List<Flashcard>
}