package com.example.apphoctap.network.api

import com.example.apphoctap.model.Submission
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubmissionApi {
    @POST("submissions")
    suspend fun submitAssignment(@Body submission: Submission): Response<Submission>

    @GET("submissions/assignment/{assignmentId}")
    suspend fun getSubmissionsByAssignment(@Path("assignmentId") assignmentId: Int): Response<List<Submission>>
}