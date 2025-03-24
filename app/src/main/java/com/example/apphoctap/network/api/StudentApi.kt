package com.example.apphoctap.network.api

import com.example.apphoctap.model.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentApi {

    @GET("students")
    suspend fun getAllStudents(): List<Student>

    @GET("students/{id}")
    suspend fun getStudentById(@Path("id") studentId: String): Student

    @POST("students")
    suspend fun createStudent(@Body student: Student): Response<Student>

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") studentId: String, @Body student: Student): Response<Student>

    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") studentId: String): Response<Unit>

}