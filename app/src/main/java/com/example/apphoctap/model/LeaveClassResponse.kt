package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LeaveClassResponse(
    val enrollmentKey: String,
    val message: String
) : Parcelable