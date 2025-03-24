package com.example.apphoctap.network.api

import com.example.apphoctap.model.ClassStudent
import com.example.apphoctap.model.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClassStudentApi {
    @POST("class-student")
    suspend fun addStudentToClass(@Body classStudent: ClassStudent): Response<Unit>

    @GET("class-student/{classId}")
    suspend fun getStudentsByClass(@Path("classId") classId: String): Response<List<Student>>

    @DELETE("class-student/{classId}/{studentId}")
    suspend fun removeStudentFromClass(@Path("classId") classId: String, @Path("studentId") studentId: String): Response<Unit>
}