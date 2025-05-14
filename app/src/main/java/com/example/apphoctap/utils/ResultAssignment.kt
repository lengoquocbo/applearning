package com.example.apphoctap.utils

sealed class ResultAssignment<out T> {
    data class Success<out T>(val data: T) : ResultAssignment<T>()
    data class Error(val message: String) : ResultAssignment<Nothing>()
    object Loading : ResultAssignment<Nothing>()

    // Helper functions
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading

    // Get data safely
    fun getDataOrNull(): T? = if (this is Success) data else null
    fun getErrorMessageOrNull(): String? = if (this is Error) message else null
}