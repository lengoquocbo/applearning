package com.example.apphoctap.network.api

import com.example.apphoctap.model.Minitest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MinitestApi {
    @POST("minitests")
    suspend fun createMinitest(@Body minitest: Minitest): Response<Minitest>

    @GET("minitests/{id}")
    suspend fun getMinitest(@Path("id") minitestId: Int): Response<Minitest>

    @GET("minitests/class/{classId}")
    suspend fun getMinitestsByClass(@Path("classId") classId: String): Response<List<Minitest>>
}