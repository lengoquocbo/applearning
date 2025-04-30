package com.example.apphoctap.model

import android.os.Parcelable
import com.example.apphoctap.enums.MessageType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message (
    val messageID: Int,
    val classID : String,
    val userID: String,
    val content:String,
    val sendAt: String,
    val fileType : MessageType,
    val fieldID: Int? = null,
    val parentMessageD : Int? = null
) : Parcelable