package com.example.apphoctap.network.api

import com.example.apphoctap.model.MinitestQuestion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MinitestQuestionApi {
    @POST("minitest-questions")
    suspend fun createQuestion(@Body question: MinitestQuestion): Response<MinitestQuestion>

    @GET("minitest-questions/minitest/{minitestId}")
    suspend fun getQuestionsByMinitest(@Path("minitestId") minitestId: Int): Response<List<MinitestQuestion>>
}
