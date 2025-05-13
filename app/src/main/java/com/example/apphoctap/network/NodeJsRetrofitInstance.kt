package com.example.apphoctap.network

import com.example.apphoctap.network.api.NodeJsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NodeJsRetrofitClient {

    private const val BASE_URL = "http://192.168.42.111:3000/"  // IP LAN hoặc domain của Node.js server

    val apiService: NodeJsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NodeJsApiService::class.java)
    }
}