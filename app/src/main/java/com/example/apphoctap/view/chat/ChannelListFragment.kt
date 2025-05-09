package com.example.apphoctap.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.apphoctap.view.ProfileNavigator
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory

class ChannelListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    val factory = ChannelViewModelFactory()
                    ChannelsScreen(
                        factory,
                        title = "Danh sách kênh chat",
                        isShowingHeader = true,
                        onBackPressed = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        },
                        onChannelClick = { channel ->
                            val intent = MessageActivity.getIntent(requireContext(), channel.cid)
                            startActivity(intent)
                        },
                        onHeaderAvatarClick = {
                            (activity as? ProfileNavigator)?.openProfile()
                        }
                    )
                }
            }
        }
    }
}
