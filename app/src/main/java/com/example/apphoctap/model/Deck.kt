package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeckRequest(
    val deckID: Long,
    val name: String,
    val dateCreate: Long,
    val userID: String
) : Parcelable


@Parcelize
data class DeckResponse(
    val deckID: Int,
    val name: String,
    val dateCreate: Long,
    val userID: String
) : Parcelable