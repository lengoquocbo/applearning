package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "flashcards",
    primaryKeys = ["flashCardID"],
    foreignKeys = [
        ForeignKey (
            entity = Assignment::class,
            parentColumns = ["assignmentID"],
            childColumns = ["assignmentID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class Flashcard(
    val flashCardID: Int,
    val assignmentID: Int,
    val question: String,
    val answer: String,
    val createdAt: String
) : Parcelable
