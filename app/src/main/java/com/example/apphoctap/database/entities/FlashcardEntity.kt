package com.example.apphoctap.database.entities

import androidx.room.*

@Entity(
    tableName = "flashcard",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["deckId"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("deckId")]
)

data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true)
    val flashcardId: Int?, // ID từ server, có thể null nếu chưa đồng bộ
    val deckId: Int,
    val frontText: String,
    val backText: String,
    val frontColor: String = "#FFFFFF", // Default white color
    val backColor: String = "#FFFFFF"   // Default white color
)

