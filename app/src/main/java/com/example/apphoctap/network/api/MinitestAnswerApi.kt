package com.example.apphoctap.network.api

import com.example.apphoctap.model.MinitestAnswer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MinitestAnswerApi {
    @POST("minitest-answers")
    suspend fun createMinitestAnswer(@Body minitestAnswer: MinitestAnswer): Response<MinitestAnswer>

    @GET("minitest-answers/{id}")
    suspend fun getMinitestAnswerById(@Path("id") id: Int): Response<MinitestAnswer>

    @GET("minitest-answers/minitest/{minitestId}")
    suspend fun getAnswersByMinitest(@Path("minitestId") minitestId: Int): Response<List<MinitestAnswer>>

    @PUT("minitest-answers/{id}")
    suspend fun updateMinitestAnswer(
        @Path("id") id: Int,
        @Body minitestAnswer: MinitestAnswer
    ): Response<MinitestAnswer>

    @DELETE("minitest-answers/{id}")
    suspend fun deleteMinitestAnswer(@Path("id") id: Int): Response<Unit>
}