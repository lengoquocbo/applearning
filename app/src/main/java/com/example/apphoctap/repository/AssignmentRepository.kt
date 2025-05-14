package com.example.apphoctap.repository

import android.util.Log
import com.example.apphoctap.model.AssignmentRequest
import com.example.apphoctap.model.AssignmentResponse
import com.example.apphoctap.model.AssignmentSubmission
import com.example.apphoctap.model.AttachmentItem
import com.example.apphoctap.model.SubmitRequest
import com.example.apphoctap.network.api.AssignmentApi
import com.example.apphoctap.network.api.DeleteResponse
import com.example.apphoctap.network.api.SubmissionApi
import com.example.apphoctap.utils.FileUploader
import com.example.apphoctap.utils.ResultAssignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssignmentRepository @Inject constructor(
    private val assignmentApi: AssignmentApi,
) {

    suspend fun createAssignment(assignmentRequest: AssignmentRequest): ResultAssignment<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Gọi API để tạo assignment
                val response = assignmentApi.createAssignment(assignmentRequest)

                if (response.isSuccessful) {
                    // Lấy thông báo thành công từ response body
                    val assignmentResponse = response.body()
                    if (assignmentResponse?.success == true) {
                        ResultAssignment.Success(assignmentResponse.message)
                    } else {
                        // Trường hợp server trả về success = false
                        ResultAssignment.Error(assignmentResponse?.message ?: "Unknown error")
                    }
                } else {
                    // Nếu không thành công, trả về thông báo lỗi từ response
                    val error = response.errorBody()?.string()
                    ResultAssignment.Error("Failed to create assignment: $error")
                }
            } catch (e: Exception) {
                // Xử lý ngoại lệ và trả về thông báo lỗi
                ResultAssignment.Error("Exception occurred: ${e.message}")
            }
        }
    }

    suspend fun deleteAssignment(assignmentId: Int): ResultAssignment<DeleteResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = assignmentApi.deleteAssignment(assignmentId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        ResultAssignment.Success(it)
                    } ?: ResultAssignment.Error("Response body is null")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    ResultAssignment.Error("Failed to delete assignment: $errorBody")
                }
            } catch (e: Exception) {
                ResultAssignment.Error("Exception while deleting assignment: ${e.message}")
            }
        }
    }


    suspend fun getAssignmentsByClassId(classId: String): ResultAssignment<List<AssignmentSubmission>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = assignmentApi.getAssignmentsbyClassId(classId)
                if (response.isSuccessful) {
                    response.body()?.let { ResultAssignment.Success(it) } ?: ResultAssignment.Success(emptyList())
                } else {
                    val error = response.errorBody()?.string()
                    ResultAssignment.Error("Failed to fetch assignments: $error")
                }
            } catch (e: Exception) {
                Log.d("Exception", e.message.toString())
                ResultAssignment.Error("Exception occurred ${e.message}")
            }
        }
    }

}