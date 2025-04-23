package com.example.apphoctap.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.apphoctap.model.ExposedUser

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_USER_EMAIL = "USER_EMAIL"
        private const val KEY_USER_ROLE = "USER_ROLE"
        private const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
    }

    fun saveUserSession(accessToken: String, refreshToken: String, role: String) {
        // Giải mã token để lấy thông tin
        val payload = JwtUtils.decodeJwt(accessToken)

        if (payload != null) {
            sharedPreferences.edit().apply {
                putString(KEY_ACCESS_TOKEN, accessToken)
                putString(KEY_REFRESH_TOKEN, refreshToken)
                putString(KEY_USER_ID, payload.optString("userID", ""))
                putString(KEY_USER_EMAIL, payload.optString("email", ""))
                putString(KEY_USER_ROLE, role) // Lưu role từ phản hồi API
                putBoolean(KEY_IS_LOGGED_IN, true)
                apply()
            }
        }
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        // Cập nhật tokens và đồng thời cập nhật thông tin từ token mới
        val payload = JwtUtils.decodeJwt(accessToken)

        sharedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)

            if (payload != null) {
                putString(KEY_USER_ID, payload.optString("userID", ""))
                putString(KEY_USER_EMAIL, payload.optString("email", ""))
                // Không cập nhật role vì chúng ta giả định role không thay đổi
            }

            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun getUserDetails(): ExposedUser {
        return ExposedUser(
            userID = sharedPreferences.getString(KEY_USER_ID, "") ?: "",
            username = sharedPreferences.getString(KEY_USER_EMAIL, "")?.substringBefore('@') ?: "", // Tạo username từ email
            email = sharedPreferences.getString(KEY_USER_EMAIL, "") ?: "",
            password = "", // Không lưu mật khẩu
            role = sharedPreferences.getString(KEY_USER_ROLE, "") ?: "",
            sdt = "" // Không có sdt trong token
        )
    }

    fun getUserRole(): String {
        return sharedPreferences.getString(KEY_USER_ROLE, "") ?: ""
    }

    fun clearSession() {
        sharedPreferences.edit().apply {
            clear() // Xóa tất cả thông tin
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun hasValidToken(): Boolean {
        val token = getAccessToken()
        return !token.isNullOrEmpty() && !JwtUtils.isTokenExpired(token)
    }

}