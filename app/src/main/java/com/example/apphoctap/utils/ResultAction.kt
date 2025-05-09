package com.example.apphoctap.utils

sealed class ResultAction<out T> {
    data class Success<T>(val data: T) : ResultAction<T>()
    data class Error(val error: ErrorType) : ResultAction<Nothing>()
    object Loading : ResultAction<Nothing>() // nếu bạn muốn hỗ trợ loading
}

