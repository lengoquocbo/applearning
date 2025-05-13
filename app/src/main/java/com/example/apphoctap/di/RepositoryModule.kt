package com.example.apphoctap.di

import com.example.apphoctap.database.dao.ClassCacheDao
import com.example.apphoctap.database.dao.DeckDao
import com.example.apphoctap.database.dao.FlashcardDao
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.network.api.DeckApi
import com.example.apphoctap.network.api.FlashCardApi
import com.example.apphoctap.repository.ClassRepository
import com.example.apphoctap.repository.DeckRepository
import com.example.apphoctap.repository.FlashcardRepository
import com.example.apphoctap.utils.NetworkMonitor
import com.example.apphoctap.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideClassRepository(
        classCacheDao: ClassCacheDao,
        classApi: ClassApi,
        networkMonitor: NetworkMonitor,
        classStudentApi : ClassStudentApi,
        sessionManager: SessionManager,
        client: ChatClient

    ): ClassRepository {
        return ClassRepository(classCacheDao, classApi, networkMonitor, classStudentApi, sessionManager, client)
    }


    @Provides
    @Singleton
    fun provideDeckRepository(
    deckDao: DeckDao,
    deckApi: DeckApi,
    networkMonitor: NetworkMonitor,
    ): DeckRepository {
    return DeckRepository(deckDao, deckApi, networkMonitor)
    }


    @Provides
    @Singleton
    fun provideFlashcardRepository(
        flashcardDao: FlashcardDao,
        flashCardApi: FlashCardApi,
        networkMonitor: NetworkMonitor
    ): FlashcardRepository {
        return FlashcardRepository(flashcardDao, flashCardApi, networkMonitor)
    }
}
