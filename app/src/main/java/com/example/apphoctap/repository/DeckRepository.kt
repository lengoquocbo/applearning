package com.example.apphoctap.repository

import androidx.lifecycle.LiveData
import com.example.apphoctap.database.dao.DeckDao
import com.example.apphoctap.database.entities.DeckEntity
import com.example.apphoctap.database.entities.toDeckEntity
import com.example.apphoctap.model.DeckRequest
import com.example.apphoctap.model.DeckResponse
import com.example.apphoctap.network.api.DeckApi
import com.example.apphoctap.utils.NetworkMonitor
import javax.inject.Inject

class DeckRepository @Inject constructor(
    private val deckDao: DeckDao,
    private val deckApi: DeckApi,
    private val networkMonitor: NetworkMonitor
) {

    // ROOM: LOCAL DATA
    fun getAllDecks(userId: String): LiveData<List<DeckEntity>> {
        return deckDao.getAllDecks(userId)
    }

    suspend fun insertDeck(name: String, userId: String): Long {
        val deck = DeckEntity(
            name = name,
            userId = userId,
            dateCreated = System.currentTimeMillis()
        )
        return deckDao.insertDeck(deck)
    }

    // This method is fixed, changed DeckResponse to DeckEntity
    suspend fun insertDeckRoom(deckResponse: DeckResponse): Long {
        // Converting DeckResponse to DeckEntity
        val deckEntity = deckResponse.toDeckEntity()
        return deckDao.insertDeck(deckEntity)
    }

    suspend fun updateDeck(deck: DeckEntity) {
        deckDao.updateDeck(deck)
    }

    suspend fun deleteDeck(deck: DeckEntity) {
        deckDao.deleteDeck(deck)
    }

    suspend fun getDeckById(deckId: Long): LiveData<DeckEntity?> {
        return deckDao.getDeckById(deckId)
    }

    // Lưu dữ liệu vào Room
    suspend fun saveDecks(decks: List<DeckResponse>) {
        deckDao.insertDeckRoom(decks.map { it.toDeckEntity() })
    }

    // API: NETWORK CALLS

    suspend fun createDeckOnline(deckRequest: DeckRequest): Result<List<DeckResponse>> {
        return try {
            if (networkMonitor.isNetworkAvailable()) {
                val response = deckApi.createDeckByUserID(deckRequest)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to create deck: ${response.message()}"))
                }
            } else {
                Result.failure(Exception("No internet connection"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllDecksOnline(userId: String): Result<List<DeckResponse>> {
        return try {
            if (networkMonitor.isNetworkAvailable()) {
                val response = deckApi.getAllDecksByUserID(userId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch decks: ${response.message()}"))
                }
            } else {
                Result.failure(Exception("KHÔNG KẾT NỐI INTERNET"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDeckOnline(deckId: Int): Result<String> {
        return try {
            if (networkMonitor.isNetworkAvailable()) {
                val response = deckApi.delteDeck(deckId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to delete deck: ${response.message()}"))
                }
            } else {
                Result.failure(Exception("KHÔNG KẾT NỐI INTERNET"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
