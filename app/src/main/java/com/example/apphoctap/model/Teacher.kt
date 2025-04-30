package com.example.apphoctap.model

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Teacher(
    val teacherID:String,
    val userID: String,
    val name:String,
): Parcelable