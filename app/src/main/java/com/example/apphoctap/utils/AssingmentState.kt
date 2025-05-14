package com.example.apphoctap.utils

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val fileIds: List<Int>) : UploadState()
    data class Error(val message: String) : UploadState()
}

sealed class AssignmentState {
    object Idle : AssignmentState()
    object Creating : AssignmentState()
    data class Created(val assignmentId: String) : AssignmentState()
    data class Error(val message: String) : AssignmentState()
}
