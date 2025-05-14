package com.example.apphoctap.repository

import com.example.apphoctap.model.SubmissionsResponse
import com.example.apphoctap.model.SubmitRequest
import com.example.apphoctap.network.api.FeedbackRequest
import com.example.apphoctap.network.api.SubmissionApi
import com.example.apphoctap.utils.ResultAssignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubmissionRepository(private val submissionApi: SubmissionApi) {

    suspend fun createSubmission(submitRequest: SubmitRequest) : ResultAssignment<String> {
        return withContext(Dispatchers.IO){
            try {
                val response = submissionApi.submitAssignment(submitRequest)

                if (response.isSuccessful) {
                    val submissionResponse = response.body()
                    if (submissionResponse?.success == true) {
                        ResultAssignment.Success(submissionResponse.message)
                    } else {
                        ResultAssignment.Error(submissionResponse?.message ?: "Unknown error")
                    }
                } else {
                    val error = response.errorBody()?.string()
                    ResultAssignment.Error("Failed to create submission: $error")
                }
            } catch (e : Exception){
                ResultAssignment.Error("Exception occurred: ${e.message}")
            }
        }
    }

    suspend fun getSubmissionsByAssignmentId(assignmentId: Int): ResultAssignment<List<SubmissionsResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = submissionApi.getSubmissionsByAssignmentId(assignmentId)

                if (response.isSuccessful) {
                    val submissions = response.body()
                    if (submissions != null) {
                        ResultAssignment.Success(submissions)
                    } else {
                        ResultAssignment.Error("No submissions found")
                    }
                } else {
                    val error = response.errorBody()?.string()
                    ResultAssignment.Error("Failed to fetch submissions: $error")
                }
            } catch (e: Exception) {
                ResultAssignment.Error("Exception occurred: ${e.message}")
            }
        }
    }

    suspend fun sendFeedback(submissionId: Int, feedback: String) {
        val request = FeedbackRequest(feedback)
        submissionApi.sendFeedback(submissionId, request)
    }

}