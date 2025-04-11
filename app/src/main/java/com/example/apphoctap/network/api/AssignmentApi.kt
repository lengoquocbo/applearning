package com.example.apphoctap.network.api

import com.example.apphoctap.model.Assignment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AssignmentApi {

    @GET("assignments")
    suspend fun getAllAssingment() : List<Assignment>

    @GET("assignments")
    suspend fun getAssignmentByClassId(@Query("classId") ClassId : String) : Response<List<Assignment>>

    @POST("assignments")
    suspend fun createAssignment(@Body assignment: Assignment) : Response<Assignment>

    @PUT("assignments/{id}")
    suspend fun updateAssignment(@Path("id") assignmentId : Int, @Body assignment: Assignment) : Response<Assignment>

    @DELETE("assignments/{id}")
    suspend fun deleteAssignment(@Path("id") assignmentId : Int) : Response<Unit>

}