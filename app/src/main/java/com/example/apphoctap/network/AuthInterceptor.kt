package com.example.apphoctap.network

import android.content.Context
import android.util.Log
import com.example.apphoctap.model.RefreshTokenRequest
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptorHilt @Inject constructor(context: Context) : Interceptor {

    private val sessionManager: SessionManager = SessionManager(context)

    companion object {
        private const val TAG = "AuthInterceptor"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Kiểm tra xem người dùng đã đăng nhập và có token hợp lệ không
        if (sessionManager.isLoggedIn()) {
            // Lấy token hiện tại
            val accessToken = sessionManager.getAccessToken()

            if (!accessToken.isNullOrEmpty()) {
                // Kiểm tra token hết hạn chưa
                if (JwtUtils.isTokenExpired(accessToken)) {
                    // Nếu token hết hạn, thử refresh token
                    val refreshToken = sessionManager.getRefreshToken()
                    if (!refreshToken.isNullOrEmpty() && !JwtUtils.isTokenExpired(refreshToken)) {
                        // Thực hiện refresh token (bạn cần triển khai phương thức này)
                        // Đây là ví dụ, trong thực tế bạn cần gọi API để lấy token mới
                        val newTokens = refreshTokenSynchronously(refreshToken)
                        if (newTokens != null) {
                            // Cập nhật token mới vào SessionManager
                            sessionManager.updateTokens(newTokens.first, newTokens.second)
                            // Tạo request mới với token mới
                            return proceedWithNewToken(chain, originalRequest, newTokens.first)
                        }
                    }
                } else {
                    // Token vẫn còn hiệu lực, sử dụng nó
                    return proceedWithNewToken(chain, originalRequest, accessToken)
                }
            }
        }

        // Nếu không có token hoặc không thể refresh, gửi request gốc
        return chain.proceed(originalRequest)
    }

    private fun proceedWithNewToken(chain: Interceptor.Chain, originalRequest: Request, token: String): Response {
        val newRequest = originalRequest.newBuilder()
            .header(AUTHORIZATION_HEADER, "$TOKEN_PREFIX$token")
            .build()
        return chain.proceed(newRequest)
    }


    private fun refreshTokenSynchronously(refreshToken: String): Pair<String, String>? {
        val refreshToken = sessionManager.getRefreshToken() ?: return null

        return try {
            runBlocking {
                val authService = RetrofitInstance.authRepository
                val refreshRequest = RefreshTokenRequest(refreshToken)
                val response = authService.refreshToken(refreshRequest)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    // Lưu token mới và role
                    sessionManager.updateTokens(loginResponse.token, loginResponse.refreshToken)

                    Pair(loginResponse.token, loginResponse.role)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi refresh token: ${e.message}")
            null
        }
    }
}