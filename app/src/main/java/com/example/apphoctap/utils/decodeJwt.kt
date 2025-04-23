package com.example.apphoctap.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {

    fun decodeJwt(token: String): JSONObject? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, Charsets.UTF_8)
            JSONObject(decodedString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    // hàm kiểm tra token hết hạn chưa
    fun isTokenExpired(token: String): Boolean {
        try {
            val payload = decodeJwt(token)
            if (payload != null) {
                val expTime = payload.optLong("exp", 0)
                val currentTime = System.currentTimeMillis() / 1000

                return expTime < currentTime
            }
            return true // Nếu không giải mã được payload, coi như token đã hết hạn
        } catch (e: Exception) {
            return true
        }
    }

    fun getUserIdFromToken(token: String): String? {
        return decodeJwt(token)?.optString("userID")
    }

    fun getRoleFromToken(token: String): String? {
        return decodeJwt(token)?.optString("role")
    }

    fun getEmailFromToken(token: String): String? {
        return decodeJwt(token)?.optString("email")
    }
}
