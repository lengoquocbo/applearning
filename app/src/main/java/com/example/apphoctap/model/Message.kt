package com.example.apphoctap.model

data class Message (
    val messageID:Int,
    val userID: User,
    val classID: Classes,
    val content:String,
    val senAt: String
)