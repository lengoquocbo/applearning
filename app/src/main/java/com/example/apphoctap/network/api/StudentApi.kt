package com.example.apphoctap.network.api

import com.example.apphoctap.model.AddStudentRequest
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.Student
import com.example.apphoctap.model.StudentRequest
import com.example.apphoctap.model.StudentResponse
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

    @DELETE("students/delete/{studentID}")
    suspend fun deletestudents(@Path("studentID") studentID: String): Response<String>


    @POST("students/add")
    suspend fun addStudentByEmail(@Body addStudentRequest: AddStudentRequest): Response<StudentResponse>

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") studentId: String, @Body student: Student): Response<Student>

    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") studentId: String): Response<Unit>

}