package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "teacher")
@Parcelize
data class Teacher(
    @PrimaryKey val teacherID:String,
    val userID: String,
    val name:String,
    val birthday:String
): Parcelable