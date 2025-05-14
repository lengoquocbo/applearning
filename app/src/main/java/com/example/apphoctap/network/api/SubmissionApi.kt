package com.example.apphoctap.network.api

import android.os.Parcelable
import com.example.apphoctap.model.Submission
import com.example.apphoctap.model.SubmissionsResponse
import com.example.apphoctap.model.SubmitRequest
import kotlinx.parcelize.Parcelize
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SubmissionApi {

    @POST("submissions/create")
    suspend fun submitAssignment(@Body submitRequest: SubmitRequest): Response<SubmissionResponse>

    @GET("/submissions/{assignmentId}")
    suspend fun getSubmissionsByAssignmentId(@Path("assignmentId") assignmentId: Int): Response<List<SubmissionsResponse>>

    @PUT("submissions/{submissionId}/feedback")
    suspend fun sendFeedback(
        @Path("submissionId") submissionId: Int,
        @Body feedback: FeedbackRequest
    )
}

data class SubmissionResponse(
    val message: String,
    val success: Boolean,
)

@Parcelize
data class FeedbackRequest(
    val feedback: String
) : Parcelable