package com.example.apphoctap.enums

import kotlinx.serialization.Serializable

@Serializable
enum class FileType {
    CREATE,
    ASSIGNMENT,
    MATERIAL,
    SUBMISSION
}