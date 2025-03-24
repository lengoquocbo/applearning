package com.example.apphoctap.network.api

import com.example.apphoctap.model.Teacher
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TeacherApi {
    @GET("teachers/{id}")
    suspend fun getTeacher(@Path("id") teacherId: String): Response<Teacher>

    @GET("teachers")
    suspend fun getAllTeachers(): Response<List<Teacher>>
}