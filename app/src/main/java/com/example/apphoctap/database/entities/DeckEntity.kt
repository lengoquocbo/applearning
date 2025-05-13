package com.example.apphoctap.database.entities

import androidx.room.*
import com.example.apphoctap.model.DeckResponse

@Entity(tableName = "deck")
data class DeckEntity(
    @PrimaryKey(autoGenerate = true)
    val deckId: Int = 0, // Mặc định để không cần truyền
    val name: String,
    val userId: String,
    val dateCreated: Long
)


fun DeckResponse.toDeckEntity(): DeckEntity {
    return DeckEntity(
        deckId = this.deckID,
        name = this.name,
        dateCreated = this.dateCreate,
        userId = this.userID
    )
}
