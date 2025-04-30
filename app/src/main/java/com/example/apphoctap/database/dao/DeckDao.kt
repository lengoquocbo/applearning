package com.example.apphoctap.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy
import com.example.apphoctap.database.entities.DeckEntity

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: DeckEntity): Long

    @Update
    suspend fun updateDeck(deck: DeckEntity)

    @Query("SELECT * FROM deck")
    suspend fun getAllDecks(): List<DeckEntity>

    @Query("SELECT * FROM deck WHERE isSynced = 0")
    suspend fun getUnsyncedDecks(): List<DeckEntity>

    @Query("SELECT * FROM deck WHERE deckId = :deckId")
    suspend fun getDeckById(deckId: Int): DeckEntity?

    @Delete
    suspend fun deleteDeck(deck: DeckEntity)
}