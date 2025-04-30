package com.example.apphoctap.network.api

import com.example.apphoctap.model.Class
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClassesApi {

    @POST("classes")
    suspend fun createClass(@Body classData: Class): Response<Class>

    @GET("classes/{id}")
    suspend fun getClass(@Path("id") classId: String): Response<Class>

    @GET("classes")
    suspend fun getAllClasses(): Response<List<Class>>

    @PUT("classes/{id}")
    suspend fun updateClass(@Path("id") classId: String, @Body classData: Class): Response<Class>

    @DELETE("classes/{id}")
    suspend fun deleteClass(@Path("id") classId: String): Response<Unit>

}