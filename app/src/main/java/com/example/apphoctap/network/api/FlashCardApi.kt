package com.example.apphoctap.network.api

import com.example.apphoctap.database.entities.FlashcardEntity
import com.example.apphoctap.model.CreateFlashcardRequest
import com.example.apphoctap.model.FlashCardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FlashCardApi {
    @POST("decks/flashcards/add")
    suspend fun createFlashCard(@Body flashcardRequest: CreateFlashcardRequest): Response<FlashCardResponse>

    @GET("decks/flashcards/get/{deckID}")
    suspend fun getAllflashcardByDeckID(@Path("deckID") deckID:String): Response<List<FlashCardResponse>>

    @DELETE("flashcards/{id}")
    suspend fun deleteFlashCard(@Path("id") flashCardId: Int): Response<Unit>
}