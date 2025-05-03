package com.example.apphoctap.model

data class ClassUiModel(
    val classId: String,
    val className: String,
    val teacherName: String,
    val description: String,
    val enrollmentKey: String,
    val isFromCache: Boolean = false
)
