package com.example.apphoctap.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Assignment  (
    val assignmentID:Int,
    val classID: String,
    val title: String,
    val description : String,
    val createAT:String,
    val dueDate:String,
    val status : String,
    val attachmentIDs : String
) : Parcelable
