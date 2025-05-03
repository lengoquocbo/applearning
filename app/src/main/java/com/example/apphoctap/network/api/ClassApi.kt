package com.example.apphoctap.network.api

import com.example.apphoctap.model.ClassResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassApi {

    @POST("classes")
    suspend fun createClass(@Body classResponseData: ClassResponse): Response<ClassResponse>

    @GET("classes/{id}")
    suspend fun getClass(@Path("id") classId: String): Response<ClassResponse>

    @GET("classes")
    suspend fun getClassByEnrollmentKey(
        @Query("enrollment_key") enrollmentKey: String
    ): Response<ClassResponse>

    @GET("classes")
    suspend fun getAllClasses(
        @Query("teacher_id") teacherId: String? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<ClassResponse>>

    @PUT("classes/{id}")
    suspend fun updateClass(
        @Path("id") classId: String,
        @Body classResponseData: ClassResponse
    ): Response<ClassResponse>

    @DELETE("classes/{id}")
    suspend fun deleteClass(@Path("id") classId: String): Response<Unit>

}