package com.example.apphoctap.repository

import com.example.apphoctap.model.CreateClassRequest
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.CreateClassResponse
import com.example.apphoctap.model.EnrollmentKeyRessponse
import com.example.apphoctap.model.StudentResponse
import com.example.apphoctap.network.api.ClassApi
import retrofit2.Response

class ClassesRepository(
    private val classesApi: ClassApi
) {
    suspend fun createClass(request: CreateClassRequest): Response<CreateClassResponse> {
        return classesApi.createClass(request)
    }

    suspend fun getClassesByTeacherID(teacherID: String): Response<List<ClassResponse>> {
        return classesApi.getClassesByTeacherID(teacherID)
    }
    suspend fun deleteClassTeacher(classID: String): Response<EnrollmentKeyRessponse>{
        return classesApi.deleteClassTeacher(classID)
    }
    suspend fun getStudentByclassID(classID: String): Response<List<StudentResponse>> {
        return classesApi.getStudentByclassID(classID)
    }
}
