package com.example.apphoctap.model

import com.example.apphoctap.enums.Role

data class AuthResponse(
    val userID: String,
    val name: String,
    val email: String,
    val role: Role, // "student" hoặc "teacher"
    val token: String
)

