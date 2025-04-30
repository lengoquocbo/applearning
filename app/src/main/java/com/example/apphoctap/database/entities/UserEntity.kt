package com.example.apphoctap.database.entities

import androidx.room.*

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val email: String,
    val phone: String,
    val role: Int // 0: học sinh, 1: giáo viên
)