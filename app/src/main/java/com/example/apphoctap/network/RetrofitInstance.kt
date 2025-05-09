package com.example.apphoctap.network

import com.example.apphoctap.network.api.AuthApi
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.network.api.StudentApi
import com.example.apphoctap.repository.AuthRepository
import com.example.apphoctap.repository.ClassesRepository
import com.example.apphoctap.repository.StudentRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.34.100:8080/"


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    val authRepository: AuthRepository by lazy {
        AuthRepository(authApi)
    }

    val classesApi: ClassApi by lazy {
        retrofit.create(ClassApi::class.java)
    }

  val classRepository: ClassesRepository by lazy {
       ClassesRepository(classesApi)
 }
    val studentApi: StudentApi by lazy {
        retrofit.create(StudentApi::class.java)
    }
    val studentRepositoy: StudentRepository by lazy {
        StudentRepository(studentApi)
    }
}
