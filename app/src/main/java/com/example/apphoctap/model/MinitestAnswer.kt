package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity (
    tableName = "minitestAnswer",
    primaryKeys = ["answerID"],
    foreignKeys = [
        ForeignKey(
            entity = MinitestQuestion::class,
            parentColumns = ["questionID"],
            childColumns = ["questionID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class MinitestAnswer(
    val answerID : Int,
    val questionID : Int,
    val answerText : String,
    val isCorrect : Boolean
) : Parcelable
