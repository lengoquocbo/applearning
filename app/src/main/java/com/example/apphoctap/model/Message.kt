package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message (
    val messageID:Int,
    val userID: User,
    val classID: Classes,
    val content:String,
    val senAt: String
) : Parcelable