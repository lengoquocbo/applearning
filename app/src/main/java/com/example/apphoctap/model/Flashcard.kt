package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize




@Parcelize
data class CreateFlashcardRequest(
    val flashcardID: Int?,
    val deckID: Int,
    val frontText: String,
    val backText: String,
    val frontColor:String,
    val backColor: String
): Parcelable

@Parcelize
data class FlashCardResponse(
    val flashcardID: Int?, // ID từ server, có thể null nếu chưa đồng bộ
    val deckID: Int,
    val frontText: String,
    val backText: String,
    val frontColor:String,
    val backColor: String
): Parcelable