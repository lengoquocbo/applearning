package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrueFalseQuestion(
    val questionID: String,
    val question: String,
    val isTrue: Boolean,
    val explanation: String,
    val difficulty: String,
    val tags: String,
    val fileID : Int
) :  Parcelable