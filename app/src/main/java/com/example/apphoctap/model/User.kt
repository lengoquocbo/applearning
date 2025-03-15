package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userID:String,
    val username:String,
    val password:String,
    val sdt:String,
    val email:String,
    val role:Int
) : Parcelable
