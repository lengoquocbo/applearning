package com.example.apphoctap.network.api

import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.CreateClassRequest
import com.example.apphoctap.model.CreateClassResponse
import com.example.apphoctap.model.EnrollmentKeyRessponse
import com.example.apphoctap.model.ExposedClass
import com.example.apphoctap.model.StudentResponse
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

    @GET("class/get")
    suspend fun getAllClass(): Response<List<ExposedClass>>

    @GET("class/teacher/{teacherID}")
    suspend fun getClassesByTeacherID(@Path("teacherID") teacherID: String): Response<List<ClassResponse>>


    @GET("class/students/{classID}")
    suspend fun getStudentByclassID(@Path("classID") classID: String): Response<List<StudentResponse>>

    @DELETE("class/delete/{classID}")
    suspend fun deleteClassTeacher(@Path("classID") ClassID: String): Response<EnrollmentKeyRessponse>

    @GET("classes/{id}")
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



    @PUT("classes/{id}")
    suspend fun updateClass(
        @Path("id") classId: String,
        @Body classResponseData: ClassResponse
    ): Response<ClassResponse>

    @DELETE("class/{id}")
    suspend fun deleteClass(@Path("id") classId: String): Response<Unit>

}