package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class EssayQuestion(
    val questionID: String,
    val question : String,
    val suggestedAnswer: String,
    val difficulty: String,
    val tags: String,
    val fileID : Int
) : Parcelable
