package com.example.apphoctap.model

import android.os.Parcelable
import com.example.apphoctap.enums.StatusCall
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoCall (
    val callID : String,
    val classID : String,
    val title : String,
    val startTime : String,
    val endTime : String,
    val status : StatusCall,
    val scheduledBy : String
) : Parcelable

