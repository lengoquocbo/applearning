package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubmitRequest(
    val assignmentId : Int,
    val fileSub : String,
    val dueDate : String
) : Parcelable

data class SubmissionsResponse(
    val submitId : Int,
    val studentId : String,
    val studentName : String,
    val uploadedAt : String,
    val fileSub : List<AttachmentResponse>,
    val feedBack : String
)