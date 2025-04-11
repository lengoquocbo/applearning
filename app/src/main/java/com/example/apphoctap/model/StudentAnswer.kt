package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "studentAnswer",
    indices = [ Index(value = ["resultID"]),
                Index(value = ["questionID"]),
                Index(value = ["answerID"]),
                Index(value = ["selectedAnswerID"])],
    primaryKeys = ["studentAnswerID"],
    foreignKeys = [
        ForeignKey(
            entity = MinitestResult::class,
            parentColumns = ["resultID"],
            childColumns = ["resultID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MinitestQuestion::class,
            parentColumns = ["questionID"],
            childColumns = ["questionID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MinitestAnswer::class,
            parentColumns = ["answerID"],
            childColumns = ["answerID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MinitestAnswer::class,
            parentColumns = ["answerID"],
            childColumns = ["selectedAnswerID"],
            onDelete = ForeignKey.SET_NULL
        )
    ]

)
@Parcelize
data class StudentAnswer(
    val studentAnswerID : Int,
    val resultID : Int,
    val questionID : Int,
    val answerID: Int?,
    val selectedAnswerID : String?
) : Parcelable