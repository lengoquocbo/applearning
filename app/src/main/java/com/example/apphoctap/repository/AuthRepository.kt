package com.example.apphoctap.repository

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
import com.example.apphoctap.network.api.AuthApi

import retrofit2.Response

class AuthRepository(private val authApi: AuthApi) {

    suspend fun Registers(user: ExposedUser): Response<ExposedUser> {
        return authApi.register(user)
    }

    suspend fun login(user: ExposedUserLogin): Response<LoginResponse> {
        return authApi.login(user)
    }
    suspend fun refreshToken(refreshRequest: RefreshTokenRequest): Response<LoginResponse> {
        return authApi.refreshToken(refreshRequest)
    }

    suspend fun sendForgotPassword(request: Exposedforget): Response<forgetRespone> {
        return authApi.forgotPassword(request)
    }
    suspend fun verifyCode(exposedCode: ExposedCode): Response<CodeResponse> {
        return authApi.verifyCode(exposedCode)
    }
    suspend fun resetPassword(ExposeNewPass: ExposeNewPass): Response<ResponseNewPass> {
        return authApi.resetPassword(ExposeNewPass)
    }

    suspend fun logout(): Response<Unit> {
        return authApi.logout()
    }


}
