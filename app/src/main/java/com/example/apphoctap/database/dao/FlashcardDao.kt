package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.FlashcardEntity

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcard WHERE deckId = :deckId")
    suspend fun getFlashcardsByDeck(deckId: Int): List<FlashcardEntity>

    @Query("SELECT * FROM flashcard WHERE isSynced = 0")
    suspend fun getUnsyncedFlashcards(): List<FlashcardEntity>

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)
}

