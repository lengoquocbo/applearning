package com.example.apphoctap.utils

import android.util.Base64
import android.util.Log
import org.json.JSONException
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
//    /**
//     * Lấy teacherID từ token JWT
//     * @param token JWT token
//     * @return teacherID hoặc null nếu không tìm thấy
//     */
    fun getTeacherIDFromToken(token: String): String? {
        val payload = decodeJwt(token) ?: return null

        return try {
            // Trích xuất teacherID từ payload của token
            if (payload.has("teacherID")) {
                payload.getString("teacherID")
            } else {
                null
            }
        } catch (e: JSONException) {
            Log.e("JwtUtils", "Error extracting teacherID from token: ${e.message}")
            null
        }
    }

//    /**
//     * Lấy studentID từ token JWT
//     * @param token JWT token
//     * @return studentID hoặc null nếu không tìm thấy
//     */
    fun getStudentIDFromToken(token: String): String? {
        val payload = decodeJwt(token) ?: return null

        return try {
            // Trích xuất studentID từ payload của token
            if (payload.has("studentID")) {
                payload.getString("studentID")
            } else {
                null
            }
        } catch (e: JSONException) {
            Log.e("JwtUtils", "Error extracting studentID from token: ${e.message}")
            null
        }
    }
    fun getUsernameFormToken(token: String): String? {
        val payload = decodeJwt(token) ?: return null

        return try {
            // Trích xuất studentID từ payload của token
            if (payload.has("username")) {
                payload.getString("username")
            } else {
                null
            }
        } catch (e: JSONException) {
            Log.e("JwtUtils", "Error extracting username from token: ${e.message}")
            null
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
    fun getSdtFromToken(token: String): String? {
        return decodeJwt(token)?.optString("sdt")
    }
}
