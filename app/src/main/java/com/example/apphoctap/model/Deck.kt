package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Deck(
    val deckID: Int,
    val name: String,
    val dateCreate: Long,
    val userID: String
) : Parcelable