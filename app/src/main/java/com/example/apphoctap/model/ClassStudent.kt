package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassStudent(
    val classStudentID: String,
    val classID: String,
    val studentID: String
): Parcelable