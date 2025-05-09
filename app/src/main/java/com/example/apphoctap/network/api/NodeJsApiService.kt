package com.example.apphoctap.network.api

import com.example.apphoctap.model.UserForChatVideo
import retrofit2.http.*

interface NodeJsApiService {
    @POST("chat/token")
    suspend fun getChatToken(@Body  userIdRequest : UserRequest): TokenResponse

    @POST("video/token")
    suspend fun getVideoToken(@Body userForChatVideo: UserForChatVideo): TokenResponse
}

data class TokenResponse(val token: String)

data class UserRequest(
    val userId: String,
    val name: String,
    val image: String,
    val role: String,
    val extraData: Map<String, String> // Thay vì để email độc lập, ta bọc vào extraData
)
