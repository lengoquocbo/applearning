package com.example.apphoctap.model



data class Flashcard(
    val flashcardID: Int?, // ID từ server, có thể null nếu chưa đồng bộ
    val deckID: Int,
    val frontText: String,
    val backText: String
)
