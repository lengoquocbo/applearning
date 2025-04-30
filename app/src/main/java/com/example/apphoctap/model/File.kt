package com.example.apphoctap.model

import com.example.apphoctap.enums.FileType

data class File(
    val fileID : Int,
    val userID : String,
    val classID : String = "",
    val filename : String,
    val filePath : String,
    val uploadedAt : String,
    val context : FileType
)