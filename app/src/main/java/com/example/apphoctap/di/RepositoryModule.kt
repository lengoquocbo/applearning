package com.example.apphoctap.di

import android.content.Context
import com.example.apphoctap.database.dao.ClassCacheDao
import com.example.apphoctap.database.dao.DeckDao
import com.example.apphoctap.database.dao.FlashcardDao
import com.example.apphoctap.network.api.DeckApi
import com.example.apphoctap.network.api.FlashCardApi
import com.example.apphoctap.repository.DeckRepository
import com.example.apphoctap.repository.FlashcardRepository
import com.example.apphoctap.network.api.AssignmentApi
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.network.api.FileApi
import com.example.apphoctap.network.api.SubmissionApi
import com.example.apphoctap.repository.AssignmentRepository
import com.example.apphoctap.repository.ClassRepository
import com.example.apphoctap.repository.FileRepository
import com.example.apphoctap.repository.SubmissionRepository
import com.example.apphoctap.utils.FileUploader
import com.example.apphoctap.utils.NetworkMonitor
import com.example.apphoctap.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient

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
    fun provideFileUploader(
        @ApplicationContext context: Context,
        fileApi: FileApi
    ): FileUploader {
        return FileUploader(context, fileApi)
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

    @Provides
    @Singleton
    fun provideAssignmentRepository(
        assignmentApi: AssignmentApi,
    ): AssignmentRepository {
        return AssignmentRepository( assignmentApi )
    }

    @Provides
    @Singleton
    fun provideSubmissionRepository(
        submissionApi: SubmissionApi
    ): SubmissionRepository {
        return SubmissionRepository(submissionApi)
    }

    @Provides
    @Singleton
    fun provideFileRepository(
        fileApi: FileApi,
        @ApplicationContext context: Context
    ): FileRepository {
        return FileRepository(fileApi, context)
    }

}
