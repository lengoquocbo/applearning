package com.example.apphoctap.network.api

import com.example.apphoctap.model.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageApi {
    @POST("messages")
    suspend fun sendMessage(@Body message: Message): Response<Message>

    @GET("messages/conversation/{conversationId}")
    suspend fun getMessages(@Path("conversationId") conversationId: String): Response<List<Message>>
}