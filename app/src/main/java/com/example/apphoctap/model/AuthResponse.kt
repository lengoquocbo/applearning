package com.example.apphoctap.model

import com.example.apphoctap.enums.Role

data class AuthResponse(
    val userID: String = "",
    val username: String,
    val email: String,
    val sdt:String,
    val password: String,
    val role: Role, // "student" hoặc "teacher"
)
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val error: String) : RegisterState()
}
