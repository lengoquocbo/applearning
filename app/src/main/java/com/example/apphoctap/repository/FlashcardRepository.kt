package com.example.apphoctap.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.apphoctap.database.dao.FlashcardDao
import com.example.apphoctap.database.entities.FlashcardEntity
import com.example.apphoctap.model.CreateFlashcardRequest
import com.example.apphoctap.model.FlashCardResponse
import com.example.apphoctap.network.api.FlashCardApi
import com.example.apphoctap.utils.NetworkMonitor
import javax.inject.Inject


class FlashcardRepository @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val flashCardApi: FlashCardApi,
    private val networkMonitor: NetworkMonitor
) {

    // Local database
    suspend fun getFlashcardsByDeckId(deckId: Long): List<FlashcardEntity> {
        return flashcardDao.getFlashcardsListByDeckId(deckId)
    }
     fun getALL(): LiveData<List<FlashcardEntity>>{
        return flashcardDao.getAll()
    }

    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long {
        return flashcardDao.insertFlashcard(flashcard)
    }

    suspend fun updateFlashcard(flashcard: FlashcardEntity) {
        flashcardDao.updateFlashcard(flashcard)
    }

    suspend fun deleteFlashcard(flashcard: FlashcardEntity) {
        flashcardDao.deleteFlashcard(flashcard)
    }

    // API - Server
    suspend fun createFlashcardOnServer(flashcardRequest: CreateFlashcardRequest): Result<FlashCardResponse> {
        if (!networkMonitor.isNetworkAvailable()) {
            return Result.failure(Exception("Không có kết nối mạng"))
        }

        return try {
            val response = flashCardApi.createFlashCard(flashcardRequest)
            if (response.isSuccessful) {
                val flashcardResponse = response.body()!!

                // Chuyển đổi từ FlashCardResponse sang FlashcardEntity
                val flashcardEntity = FlashcardEntity(
                    flashcardId = flashcardResponse.flashcardID,
                    deckId = flashcardResponse.deckID,
                    frontText = flashcardResponse.frontText,
                    backText = flashcardResponse.backText,
                    frontColor = flashcardResponse.frontColor,
                    backColor = flashcardResponse.backColor
                )

                // Lưu vào Room
                insertFlashcard(flashcardEntity)
                Log.e("FlashcardRepository", "Đã lưu vào Room")

                Result.success(flashcardResponse)
            } else {
                Result.failure(Exception("Tạo thất bại: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }





    suspend fun getFlashcardsFromServer(deckId: Long): Result<List<FlashCardResponse>> {
        if (!networkMonitor.isNetworkAvailable()) {
            // Khi không có mạng, lấy dữ liệu từ Room (phải là suspend DAO function)
            return try {
                val localFlashcards = flashcardDao.getFlashcardsListByDeckId(deckId)
                if (localFlashcards.isNotEmpty()) {
                    val flashcardResponses = localFlashcards.map { entity ->
                        FlashCardResponse(
                            flashcardID = entity.flashcardId,
                            deckID = entity.deckId,
                            frontText = entity.frontText,
                            backText = entity.backText,
                            frontColor = entity.frontColor,
                            backColor = entity.backColor
                        )
                    }
                    Log.d("FlashcardRepository", "Offline mode: Loaded ${flashcardResponses.size} flashcards from local database")
                    Result.success(flashcardResponses)
                } else {
                    Result.failure(Exception("Không có dữ liệu cục bộ"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        return try {
            val response = flashCardApi.getAllflashcardByDeckID(deckId.toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Đồng bộ thất bại: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFlashcardOnServer(flashcardId: Int): Result<Unit> {
        if (!networkMonitor.isNetworkAvailable()) {
            return Result.failure(Exception("Không có kết nối mạng"))
        }

        return try {
            val response = flashCardApi.deleteFlashCard(flashcardId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Xóa thất bại: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
