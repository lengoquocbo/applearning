package com.example.apphoctap.model

import android.os.Parcelable
import com.example.apphoctap.enums.Submit
import kotlinx.parcelize.Parcelize

@Parcelize
data class Submission(
    val submitID: Int,
    val studentID: String,
    val assignmentID: Int,
    val fileSub: String,
    val score: Int,
    val submitAt: String,
    val feedBack: String,
    val status: Submit,
    val isSynced : Boolean = false
): Parcelable



