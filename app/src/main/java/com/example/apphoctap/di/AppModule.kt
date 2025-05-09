package com.example.apphoctap.di

import android.content.Context
import com.example.apphoctap.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStatePluginFactory(@ApplicationContext context: Context): StreamStatePluginFactory {
        return StreamStatePluginFactory(
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true
            ),
            appContext = context
        )
    }

    @Provides
    @Singleton
    fun provideOfflinePluginFactory(@ApplicationContext context: Context): StreamOfflinePluginFactory {
        return StreamOfflinePluginFactory(
            appContext = context,
            now = { System.currentTimeMillis() }
        )
    }

    @Provides
    @Singleton
    fun provideChatClient(
        @ApplicationContext context: Context,
        offlinePluginFactory: StreamOfflinePluginFactory,
        statePluginFactory: StreamStatePluginFactory
    ): ChatClient {
        return ChatClient.Builder(context.getString(R.string.apiKey), context)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }

}