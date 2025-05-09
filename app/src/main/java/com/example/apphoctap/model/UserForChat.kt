package com.example.apphoctap.model

import com.example.apphoctap.enums.Role

data class UserForChatVideo(
    val id: String,
    val name: String,
    val email : String,
    val image: String,
    val role : String
)