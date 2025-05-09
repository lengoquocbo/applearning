package com.example.apphoctap.view.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)
        if (channelId == null) {
            finish()
            return
        }
        setContent {

            val viewmodelFactory = MessagesViewModelFactory(
                channelId = channelId,
                messageLimit = 30,
                context = this
            )
            ChatTheme {
                MessagesScreen(
                    viewmodelFactory,
                    onBackPressed = { finish() }
                )
            }
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context : Context, channelId : String) : Intent {
            return Intent(context, MessageActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}


