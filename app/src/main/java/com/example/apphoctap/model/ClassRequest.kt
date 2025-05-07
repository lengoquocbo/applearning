package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateClassRequest(
    val classID: String,
    val teacherID: String,
    val className: String,
    val description: String,
    val createAt : String,
    val enrollmentKey : String
) : Parcelable
@Parcelize
data class ExposedClass(
    val classID: String,
    val teacherID: String,
    val className: String,
    val description: String,
    val createAt : String,
    val enrollmentKey : String
) : Parcelable

data class UpdateClassRequest(
    val className: String,
    val description: String
)