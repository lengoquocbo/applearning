package com.example.apphoctap.network.api

import com.example.apphoctap.database.entities.FlashcardEntity
import com.example.apphoctap.model.DeckRequest
import com.example.apphoctap.model.DeckResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DeckApi {
    @POST("decks/add")
    suspend fun createDeckByUserID(@Body DeckRequest: DeckRequest): Response<List<DeckResponse>>

    @GET("decks/{userID}")
    suspend fun getAllDecksByUserID(@Path("userID") userID: String): Response<List<DeckResponse>>

    @DELETE("decks/delete/{deckID}")
    suspend fun delteDeck(@Path("deckID") deckID: Int): Response<String>
}