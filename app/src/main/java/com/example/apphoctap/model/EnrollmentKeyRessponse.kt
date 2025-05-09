package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EnrollmentKeyRessponse(
    val enrollmentKey : String,
    val message : String
) : Parcelable