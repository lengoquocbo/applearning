package com.example.apphoctap.repository

import com.example.apphoctap.model.AddStudentRequest
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.DeleteStudentRequest
import com.example.apphoctap.model.DeleteStudentResponse
import com.example.apphoctap.model.StudentResponse
import com.example.apphoctap.model.StudentResponseWithEnrollMentKey
import com.example.apphoctap.network.api.StudentApi
import retrofit2.Response

class StudentRepository(
    private  val studentApi: StudentApi
) {
   suspend fun addStudentByEmail(addStudentRequest: AddStudentRequest): Response<StudentResponseWithEnrollMentKey>{
       return studentApi.addStudentByEmail(addStudentRequest)
   }
    suspend fun deleteStudentByStudentID(deleteStudentRequest: DeleteStudentRequest): Response<DeleteStudentResponse>{
        return studentApi.deleteStudents(deleteStudentRequest)
    }
}