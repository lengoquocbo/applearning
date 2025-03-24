package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity (
    tableName = "minitestAnswer",
    primaryKeys = ["answerId"],
    foreignKeys = [
        ForeignKey(
            entity = MinitestQuestion::class,
            parentColumns = ["questionId"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class MinitestAnswer(
    val answerId : Int,
    val questionId : Int,
    val answerText : String,
    val isCorrect : Boolean
) : Parcelable
