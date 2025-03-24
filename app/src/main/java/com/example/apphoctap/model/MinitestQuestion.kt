package com.example.apphoctap.model

import java.time.LocalDateTime
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "minitestQuestion",
    primaryKeys = ["questionId"],
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
    val questionId : Int,
    val minitestsId : Int,
    val question : String,
    val createdAt : LocalDateTime
) : Parcelable
