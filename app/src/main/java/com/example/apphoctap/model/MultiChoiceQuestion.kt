package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MultiChoiceQuestion(
    val questionID: String,
    val question: String,
    val options: String,
    val correctAnswerIndex: Int,
    val explanation: String,
    val difficulty: String,
    val tags: String,
    val fileID : Int
) : Parcelable