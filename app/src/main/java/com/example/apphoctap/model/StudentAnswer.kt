package com.example.apphoctap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "studentAnswer",
    indices = [ Index(value = ["resultId"]),
                Index(value = ["questionId"]),
                Index(value = ["answerId"]),
                Index(value = ["selectedAnswerId"])],
    primaryKeys = ["studentAnswerId"],
    foreignKeys = [
        ForeignKey(
            entity = MinitestResult::class,
            parentColumns = ["resultId"],
            childColumns = ["resultId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MinitestQuestion::class,
            parentColumns = ["questionId"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MinitestAnswer::class,
            parentColumns = ["answerId"],
            childColumns = ["answerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MinitestAnswer::class,
            parentColumns = ["answerId"],
            childColumns = ["selectedAnswerId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]

)
@Parcelize
data class StudentAnswer(
    val studentAnswerId : Int,
    val resultId : Int,
    val questionId : Int,
    val answerId: Int?,
    val selectedAnswerId : String?
) : Parcelable