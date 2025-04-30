package com.example.apphoctap.model

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Class(
    val classID: String,
    val teacherID: String,
    val className: String,
    val description: String,
    val createAt : String,
    val updateAt : String,
    val enrollmentKey : String
) : Parcelable