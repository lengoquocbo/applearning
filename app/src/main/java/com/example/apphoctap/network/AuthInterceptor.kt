package com.example.apphoctap.network

import android.content.Context
import com.example.apphoctap.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionManager = SessionManager(context) // Lấy Token từ SessionManager
        val accessToken = sessionManager.getAccessToken()

        val request = chain.request().newBuilder()
            .apply {
                if (!accessToken.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $accessToken") // Thêm Token vào Header
                    addHeader("Content-Type", "application/json")
                    addHeader("accept", "application/json")
                }
            }
            .build()

        return chain.proceed(request) // Tiếp tục request với Header mới
    }
}