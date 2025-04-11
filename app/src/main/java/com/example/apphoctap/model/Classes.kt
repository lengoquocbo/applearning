package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize

@Entity(
    tableName = "classes",
    foreignKeys = [
        ForeignKey(
            entity = Teacher::class,
            parentColumns = ["teacherID"],
            childColumns = ["teacherID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Classes(
    @PrimaryKey val classID:String,
    val teacherID: String,
    val className: String
) : Parcelable