package com.example.apphoctap.repository

import com.example.apphoctap.model.CreateClassRequest
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.CreateClassResponse
import com.example.apphoctap.network.api.ClassApi
import retrofit2.Response

class ClassesRepository (
    private val classesApi: ClassApi
) {
    suspend fun CreateClass(CreateClassRequest: CreateClassRequest): Response<CreateClassResponse> {
        return classesApi.createClass(CreateClassRequest)
    }

}
