package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "student")
@Parcelize
data class Student (
    @PrimaryKey val studentID:String,
    val userID: String,
    val name:String,
) : Parcelable