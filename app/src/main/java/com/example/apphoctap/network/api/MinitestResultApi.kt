package com.example.apphoctap.network.api

import com.example.apphoctap.model.MinitestResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MinitestResultApi {
    @POST("minitest-results")
    suspend fun submitResult(@Body result: MinitestResult): Response<MinitestResult>

    @GET("minitest-results/minitest/{minitestId}")
    suspend fun getResultsByMinitest(@Path("minitestId") minitestId: Int): Response<List<MinitestResult>>
}