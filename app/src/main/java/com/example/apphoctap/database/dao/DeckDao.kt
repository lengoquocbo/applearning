package com.example.apphoctap.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy
import com.example.apphoctap.database.entities.DeckEntity

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: DeckEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeckRoom(decks: List<DeckEntity>)

    @Query("SELECT * FROM deck WHERE userID = :userId ORDER BY dateCreated DESC")
    fun getAllDecks(userId: String): LiveData<List<DeckEntity>>


    @Query("SELECT * FROM deck WHERE deckId = :deckId")
    fun getDeckById(deckId: Long): LiveData<DeckEntity?>

    @Update
    suspend fun updateDeck(deck: DeckEntity)

    @Delete
    suspend fun deleteDeck(deck: DeckEntity)
}