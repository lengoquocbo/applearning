package com.example.apphoctap.model

import java.time.LocalDateTime
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "flashcards",
    primaryKeys = ["flashCardId"],
    foreignKeys = [
        ForeignKey (
            entity = Assignment::class,
            parentColumns = ["assignmentId"],
            childColumns = ["assignmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class Flashcard(
    val flashCardId: Int,
    val assignmentId: Int,
    val question: String,
    val answer: String,
    val createdAt: LocalDateTime
) : Parcelable
