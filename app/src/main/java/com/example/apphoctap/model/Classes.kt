package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "classes")
data class Classes(
    @PrimaryKey val classID:String,
    val teacherID: String,
    val className: String
) : Parcelable