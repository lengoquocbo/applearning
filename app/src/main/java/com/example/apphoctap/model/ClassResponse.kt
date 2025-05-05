package com.example.apphoctap.model

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassResponse(
    val classID: String,
    val teacherID: String,
    val teacherName : String,
    val className: String,
    val description: String,
    val createAt : String,
    val enrollmentKey : String
) : Parcelable

data class CreateClassResponse(val message: String)