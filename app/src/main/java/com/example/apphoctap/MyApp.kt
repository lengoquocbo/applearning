package com.example.apphoctap

//import android.annotation.SuppressLint
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
//import io.getstream.chat.android.client.ChatClient
//import io.getstream.chat.android.client.models.User
//import io.getstream.video.android.core.GEO
//import io.getstream.video.android.core.StreamVideo
//import io.getstream.video.android.model.User as VideoUser
//import io.getstream.video.android.core.StreamVideoBuilder

@HiltAndroidApp
class MyApp : Application(){
//    companion object {
//        lateinit var chatClient: ChatClient
//            private set
//        lateinit var streamVideo: StreamVideo
//            private set
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Initialize Stream Chat Client
//        chatClient = ChatClient.Builder("5q6ebvvwua3p", applicationContext).build()
//
//        // Connect user to Stream Chat
//        val Chatuser = User(id = "user_id", name = "User Name")
//        chatClient.connectUser(Chatuser, "w42vtdabsfrganahrb9p9fpyd6rewwee69uq5w5sqatc2acc2fetpzf99eyswxbs").enqueue { result ->
//            if (result.isSuccess) {
//                // User connected successfully
//            } else {
//                // Handle error (e.g., log error)
//            }
//        }
//
//        val videoUser = VideoUser(id = "user_id", name = "User Name")
//        val client = StreamVideoBuilder(
//            context = this,
//            apiKey = "5q6ebvvwua3p",
//            geo = GEO.GlobalEdgeNetwork,
//            user = videoUser,
//            token = "w42vtdabsfrganahrb9p9fpyd6rewwee69uq5w5sqatc2acc2fetpzf99eyswxbs",
//        ).build()
//    }
//
//    @SuppressLint("CheckResult")
//    override fun onTerminate() {
//        super.onTerminate()
//        // Disconnect chat client
//        chatClient.disconnect(true)
//        // Clean up video client
//        streamVideo.cleanup()
//    }
}