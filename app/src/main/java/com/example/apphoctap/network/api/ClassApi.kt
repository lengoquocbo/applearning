package com.example.apphoctap.network.api

import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.CreateClassRequest
import com.example.apphoctap.model.CreateClassResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassApi {

    @POST("class/create")
    suspend fun createClass(
        @Body CreateClassRequest: CreateClassRequest,
    ): Response<CreateClassResponse>

    @GET("class/{id}")
    suspend fun getClass(@Path("id") classId: String): Response<ClassResponse>

    @GET("class")
    suspend fun getClassByEnrollmentKey(
        @Query("enrollment_key") enrollmentKey: String
    ): Response<ClassResponse>

    @GET("class")
    suspend fun getAllClasses(
        @Query("teacher_id") teacherId: String? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<ClassResponse>>

    @GET("class/student")
    suspend fun getClassByStudentId():Response<List<ClassResponse>>

    @PUT("class/{id}")
    suspend fun updateClass(
        @Path("id") classId: String,
        @Body classResponseData: ClassResponse
    ): Response<ClassResponse>

    @DELETE("class/{id}")
    suspend fun deleteClass(@Path("id") classId: String): Response<Unit>

}