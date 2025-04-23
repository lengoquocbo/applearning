

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
import java.io.IOException
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {

    private val sessionManager = SessionManager(context)

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Nếu request không cần token
        if (!requiresAuthentication(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        // Lấy accessToken hiện tại
        val accessToken = sessionManager.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            Log.d(TAG, "Không có access token, tiếp tục request không có header Authorization")
            return chain.proceed(originalRequest)
        }

        // Kiểm tra nếu token hết hạn thì refresh
        if (JwtUtils.isTokenExpired(accessToken)) {
            Log.d(TAG, "Access token đã hết hạn, thực hiện refresh token")
            val refreshResult = refreshToken()

            if (refreshResult == null) {
                // Refresh token thất bại, yêu cầu đăng nhập lại
                Log.d(TAG, "Refresh token thất bại, xóa phiên làm việc")
                sessionManager.clearSession()

                // Broadcast event để UI hiển thị màn hình đăng nhập
                // (có thể triển khai với LocalBroadcastManager)

                return chain.proceed(originalRequest)
            }

            // Tiếp tục với token mới
            Log.d(TAG, "Refresh token thành công, sử dụng token mới")
            return chain.proceed(addAuthorizationHeader(originalRequest, refreshResult.first))
        }

        // Token vẫn còn hiệu lực, thêm vào header
        Log.d(TAG, "Access token vẫn còn hiệu lực, thêm vào header")
        return chain.proceed(addAuthorizationHeader(originalRequest, accessToken))
    }

    private fun requiresAuthentication(request: Request): Boolean {
        val path = request.url().encodedPath()
        // Loại trừ các endpoint không cần token
        return !path.contains("auth/login") &&
                !path.contains("auth/register") &&
                !path.contains("auth/refreshtoken")
    }

    private fun addAuthorizationHeader(request: Request, token: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }

    // Trả về Pair<accessToken, role> hoặc null nếu thất bại
    private fun refreshToken(): Pair<String, String>? {
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