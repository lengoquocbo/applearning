package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "minitestQuestion",
    primaryKeys = ["questionID"],
    foreignKeys = [
        ForeignKey(
            entity = Minitest::class,
            parentColumns = ["minitestID"],
            childColumns = ["minitestID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class MinitestQuestion(
    val questionID : Int,
    val minitestID : Int,
    val question : String,
    val createdAt : String
) : Parcelable
