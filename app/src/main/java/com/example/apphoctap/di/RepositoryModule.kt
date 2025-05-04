package com.example.apphoctap.di

import com.example.apphoctap.database.dao.ClassCacheDao
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.repository.ClassRepository
import com.example.apphoctap.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideClassRepository(
        classCacheDao: ClassCacheDao,
        classApi: ClassApi,
        networkMonitor: NetworkMonitor,
        classStudentApi : ClassStudentApi
    ): ClassRepository {
        return ClassRepository(classCacheDao, classApi, networkMonitor, classStudentApi)
    }
}