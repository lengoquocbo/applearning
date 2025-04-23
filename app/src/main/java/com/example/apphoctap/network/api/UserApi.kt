package com.example.apphoctap.network.api

import com.example.apphoctap.model.ExposedUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: String): Response<ExposedUser>

    @GET("users")
    suspend fun getAllUsers(): Response<List<ExposedUser>>
}
