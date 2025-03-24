package com.example.apphoctap.network.api

import com.example.apphoctap.model.VideoCall
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VideoCallApi {
    @POST("videocalls")
    suspend fun createVideoCall(@Body videoCall: VideoCall): Response<VideoCall>

    @GET("videocalls/{id}")
    suspend fun getVideoCall(@Path("id") callId: String): Response<VideoCall>

    @GET("videocalls/class/{classId}")
    suspend fun getVideoCallsByClass(@Path("classId") classId: String): Response<List<VideoCall>>
}