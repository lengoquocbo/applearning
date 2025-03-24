package com.yourapp.network

import android.content.Context
import com.example.apphoctap.network.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://localhost:8000/"

    fun create(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Thêm Interceptor vào OkHttpClient
            .connectTimeout(30, TimeUnit.SECONDS) // Timeout kết nối
            .readTimeout(30, TimeUnit.SECONDS) // Timeout đọc dữ liệu
            .writeTimeout(30, TimeUnit.SECONDS) // Timeout ghi dữ liệu
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Sử dụng OkHttpClient với Interceptor
            .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi JSON sang Object
            .build()
    }
}
