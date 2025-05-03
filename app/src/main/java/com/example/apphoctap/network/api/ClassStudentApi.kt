package com.example.apphoctap.network.api

import com.example.apphoctap.model.ClassStudent
import com.example.apphoctap.model.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassStudentApi {
    @POST("class_student")
    suspend fun addStudentToClass(@Body classStudent: ClassStudent): Response<Unit>

    @GET("class_student")
    suspend fun getStudentsByClass(@Path("classId") classId: String): Response<List<Student>>

    @DELETE("class-student/{id}")
    suspend fun removeStudentFromClass(@Path("id") classStudentId: String): Response<Unit>

    @DELETE("class_student")
    suspend fun leaveClass(
        @Query("classId") classId: String,
        @Query("studentId") studentId: String
    ) : Response<Unit>
}