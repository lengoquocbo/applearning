package com.example.apphoctap.database.entities

import androidx.room.*

@Entity(tableName = "deck")
data class DeckEntity(
    @PrimaryKey
    val deckId: Int,
    val name: String,
    val dateCreated: Long,
    val lastSyncTime: Long, // Thời gian đồng bộ lần cuối
    val isSynced: Boolean = true // Đã đồng bộ với server hay chưa
)

