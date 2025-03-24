package com.example.apphoctap.network.api

import com.example.apphoctap.model.StudentAnswer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StudentAnswerApi {
    @POST("student-answers")
    suspend fun submitAnswer(@Body answer: StudentAnswer): Response<Unit>

    @GET("student-answers/minitest/{minitestId}")
    suspend fun getAnswersByMinitest(@Path("minitestId") minitestId: Int): Response<List<StudentAnswer>>
}