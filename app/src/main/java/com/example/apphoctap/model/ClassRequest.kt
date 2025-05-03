package com.example.apphoctap.model

data class CreateClassRequest(
    val className: String,
    val teacherId: String,
    val description: String
)

data class UpdateClassRequest(
    val className: String,
    val description: String
)