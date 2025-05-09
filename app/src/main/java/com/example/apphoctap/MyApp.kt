package com.example.apphoctap


import android.app.Application
import com.android.volley.RequestQueue
import dagger.hilt.android.HiltAndroidApp
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

@HiltAndroidApp
class MyApp : Application(){

    companion object {
        var streamVideo: StreamVideo? = null
            private set
    }

    private lateinit var queue: RequestQueue


    fun getRequestQueue(): RequestQueue = queue
    // Hàm bạn gọi sau khi login thành công
    fun initStreamVideo(user : User, token: String) {
        streamVideo = StreamVideoBuilder(
            context = this,
            apiKey = getString(R.string.apiKey),
            geo = GEO.GlobalEdgeNetwork,
            user = user,
            token = token
        ).build()
    }
}