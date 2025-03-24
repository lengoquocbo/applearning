package com.example.apphoctap.network.api

import com.example.apphoctap.model.Flashcard
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FlashCardApi {
    @POST("flashcards")
    suspend fun createFlashCard(@Body flashCard: Flashcard): Response<Flashcard>

    @GET("flashcards/{id}")
    suspend fun getFlashCard(@Path("id") flashCardId: Int): Response<Flashcard>

    @GET("flashcards/class/{classId}")
    suspend fun getFlashCardsByClass(@Path("classId") classId: String): Response<List<Flashcard>>

    @PUT("flashcards/{id}")
    suspend fun updateFlashCard(@Path("id") flashCardId: Int, @Body flashCard: Flashcard): Response<Flashcard>

    @DELETE("flashcards/{id}")
    suspend fun deleteFlashCard(@Path("id") flashCardId: Int): Response<Unit>
}