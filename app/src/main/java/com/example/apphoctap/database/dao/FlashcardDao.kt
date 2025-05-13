package com.example.apphoctap.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.apphoctap.database.entities.FlashcardEntity

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long

    @Query("SELECT * FROM flashcard WHERE deckId = :deckId")
    suspend fun getFlashcardsListByDeckId(deckId: Long): List<FlashcardEntity>


    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcard")
     fun getAll(): LiveData<List<FlashcardEntity>>
}


