package com.example.apphoctap.repository

import com.example.apphoctap.model.AddStudentRequest
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.StudentResponse
import com.example.apphoctap.network.api.StudentApi
import retrofit2.Response

class StudentRepository(
    private  val studentApi: StudentApi
) {
   suspend fun addStudentByEmail(addStudentRequest: AddStudentRequest): Response<StudentResponse>{
       return studentApi.addStudentByEmail(addStudentRequest)
   }
    suspend fun deleteStudentByStudentID(studentID: String): Response<String>{
        return studentApi.deletestudents(studentID)
    }
}