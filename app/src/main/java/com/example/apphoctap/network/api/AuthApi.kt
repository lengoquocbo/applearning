package com.example.apphoctap.network.api

import com.example.apphoctap.model.CodeResponse
import com.example.apphoctap.model.ExposeNewPass
import com.example.apphoctap.model.ExposedCode
import com.example.apphoctap.model.ExposedUser
import com.example.apphoctap.model.ExposedUserLogin
import com.example.apphoctap.model.Exposedforget
import com.example.apphoctap.model.LoginResponse
import com.example.apphoctap.model.RefreshTokenRequest
import com.example.apphoctap.model.ResponseNewPass
import com.example.apphoctap.model.forgetRespone
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/login")
    suspend fun login(@Body loginRequest: ExposedUserLogin): Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: ExposedUser): Response<ExposedUser>

    // API lấy token khi hết hạn token
    @POST("users/refreshtoken")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<LoginResponse>

    @POST("users/forgotpassword")
    suspend fun forgotPassword(@Body request: Exposedforget): Response<forgetRespone>
    @POST("users/verifycode")
    suspend fun verifyCode(@Body exposedCode: ExposedCode): Response<CodeResponse>

    @POST("users/resetpassword")
    suspend fun resetPassword(@Body ExposeNewPass: ExposeNewPass): Response<ResponseNewPass>

    @POST("users/logout")
    suspend fun logout(): Response<Unit>
}