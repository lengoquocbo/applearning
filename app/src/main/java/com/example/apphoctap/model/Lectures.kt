package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lectures(
    val lectureID:Int,
    val classID: Classes,
    val title:String,
    val content:String,
    val fileURL:String
) : Parcelable
