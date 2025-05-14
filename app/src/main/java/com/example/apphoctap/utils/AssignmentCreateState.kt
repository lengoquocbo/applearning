package com.example.apphoctap.utils

import com.example.apphoctap.model.Assignment
import com.example.apphoctap.model.AssignmentResponse
import com.example.apphoctap.model.FileUploadResponse


sealed class AssignmentCreationState {
    object Idle : AssignmentCreationState()
    object Loading : AssignmentCreationState()
    data class Success(val assignment: AssignmentResponse) : AssignmentCreationState()
    data class FileUploaded(val files: List<FileUploadResponse>) : AssignmentCreationState()
    data class Error(val message: String) : AssignmentCreationState()
}