package com.example.apphoctap.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
// Data class cho attachment item
data class AttachmentItem(
    val fileName: String,
    val fileUri: Uri,
    val mimeType: String? = null
) : Parcelable