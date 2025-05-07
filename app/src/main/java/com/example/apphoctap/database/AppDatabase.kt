package com.example.apphoctap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apphoctap.database.dao.*
import com.example.apphoctap.database.entities.UserEntity
import com.example.apphoctap.database.entities.*
import com.example.apphoctap.model.MultiChoiceQuestion
import com.example.apphoctap.model.TrueFalseQuestion

@Database(
    entities = [
        UserEntity::class,
        DeckEntity::class,
        FlashcardEntity::class,
        ClassCacheEntitiy::class,
        QuestionFileEntity::class,
        MultiChoiceQuestionEntity::class,
        TrueFalseQuestionEntity::class,
        EssayQuestionEntity::class
    ],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun fileDao(): QuestionFileDao
    abstract fun essayQuestionDao(): EssayQuestionDao
    abstract fun trueFalseQuestionDao(): TrueFalseQuestionDao
    abstract fun multiChoiceQuestionDao(): MultiChoiceQuestionDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun userDao(): UserDao
    abstract fun classCacheDao(): ClassCacheDao
}