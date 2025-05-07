package com.example.apphoctap.di

import android.content.Context
import androidx.room.Room
import com.example.apphoctap.database.AppDatabase
import com.example.apphoctap.database.dao.ClassCacheDao

import com.example.apphoctap.database.dao.DeckDao
import com.example.apphoctap.database.dao.EssayQuestionDao
import com.example.apphoctap.database.dao.QuestionFileDao
import com.example.apphoctap.database.dao.FlashcardDao
import com.example.apphoctap.database.dao.MultiChoiceQuestionDao
import com.example.apphoctap.database.dao.TrueFalseQuestionDao
import com.example.apphoctap.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration(true)
            .build()
    }


    @Provides fun provideDeckDao(db: AppDatabase): DeckDao = db.deckDao()
    @Provides fun provideQuestionFileDao(db: AppDatabase): QuestionFileDao = db.fileDao()
    @Provides fun provideEssayQuestionDao(db: AppDatabase): EssayQuestionDao = db.essayQuestionDao()
    @Provides fun provideTrueFalseQuestionDao(db: AppDatabase): TrueFalseQuestionDao = db.trueFalseQuestionDao()
    @Provides fun provideMultiChoiceQuestionDao(db: AppDatabase): MultiChoiceQuestionDao = db.multiChoiceQuestionDao()
    @Provides fun provideFlashcardDao(db: AppDatabase): FlashcardDao = db.flashcardDao()
    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideClassCacheDao(db: AppDatabase): ClassCacheDao = db.classCacheDao()
}
