package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

// Data classes to match API requests and responses
@Parcelize
data class AssignmentRequest(
    val classId : String,
    val title: String,
    val description: String,
    val dueDate: String,
    val attachmentIds: String
) : Parcelable

@Serializable
data class AssignmentResponse(
    val id: String,
    val title: String,  
    val description: String,
    val dueDate: String,
    val createdAt: String,
    val attachments: List<AttachmentResponse>
)

@Serializable
data class AssignmentSubmission(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val createdAt: String,
    val attachments: List<AttachmentResponse>,
    val status : String? = null,
    val score : Int? = null,
    val feedback : String? = null
)

@Serializable
data class AttachmentResponse(
    val id: String,
    val fileName: String
)

data class FileItem(
    val fileId : String,
    val fileName : String,
    val createAt : String,
    val filePath : String
)

@Parcelize
data class UpdateAssignmentRequest(
    val title : String,
    val description : String,
    val dueDate: String
) : Parcelable