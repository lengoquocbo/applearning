package com.example.apphoctap.utils

// Các loại lỗi
sealed class ErrorType
class NetworkError(val message: String) : ErrorType()
class ApiError(val message: String, val code: Int) : ErrorType()
class InvalidEnrollmentKeyError(val message: String) : ErrorType()
class AccessDeniedError(val message: String) : ErrorType()
class UnauthorizedError(val message: String) : ErrorType()
class NotFoundError(val message: String) : ErrorType()
