package com.example.apphoctap.utils

import com.example.apphoctap.model.FileItem

sealed class FileResult<out T> {
    data class Success<T>(val data: T) : FileResult<T>()
    data class Error(val message: String) : FileResult<Nothing>()
    data object Loading : FileResult<Nothing>()
    data object Empty : FileResult<Nothing>()
}

data class FileOperationResponse(
    val success: Boolean,
    val message: String,
    val affectedFiles: List<FileItem> = emptyList()
)
