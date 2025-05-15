package com.example.apphoctap.network.api

import com.example.apphoctap.model.Assignment
import com.example.apphoctap.model.AssignmentRequest
import com.example.apphoctap.model.AssignmentResponse
import com.example.apphoctap.model.AssignmentSubmission
import com.example.apphoctap.model.UpdateAssignmentRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AssignmentApi {

    @POST("assignments/create")
    suspend fun createAssignment(@Body request: AssignmentRequest): Response<AssignmentResponseMini>

    @GET("assignments/{classId}")
    suspend fun getAssignmentsbyClassId(@Path("classId") classId: String): Response<List<AssignmentSubmission>>

    @GET("/assignments/")
    suspend fun getAllAssingment() : List<Assignment>

    @GET("/assignments/")
    suspend fun getAssignmentByClassId(@Query("classId") ClassId : String) : Response<List<Assignment>>


    @PUT("/assignments/{id}")
    suspend fun updateAssignment(
        @Path("id") assignmentId : Int,
        @Body assignment: UpdateAssignmentRequest
    ) : Response<UpdateResponse>

    @DELETE("/assignments/{id}")
    suspend fun deleteAssignment(@Path("id") assignmentId : Int) : Response<DeleteResponse>

}

data class DeleteResponse(
    val message: String,
    val success: Boolean,
)
data class AssignmentResponseMini(
    val message: String,
    val success: Boolean,
)

data class UpdateResponse(
    val message: String,
    val success: Boolean,
)


