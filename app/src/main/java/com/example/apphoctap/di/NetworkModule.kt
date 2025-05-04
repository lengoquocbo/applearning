package com.example.apphoctap.di

import android.content.Context
import com.example.apphoctap.network.AuthInterceptor
import com.example.apphoctap.network.AuthInterceptorHilt
import com.example.apphoctap.network.api.AssignmentApi
import com.example.apphoctap.network.api.AuthApi
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.network.api.FlashCardApi
import com.example.apphoctap.network.api.MessageApi
import com.example.apphoctap.network.api.StudentApi
import com.example.apphoctap.network.api.SubmissionApi
import com.example.apphoctap.network.api.TeacherApi
import com.example.apphoctap.network.api.UserApi
import com.example.apphoctap.network.api.VideoCallApi
import com.example.apphoctap.utils.Constraints
import com.example.apphoctap.utils.NetworkMonitor
import com.example.apphoctap.utils.NetworkMonitorImpl
import com.example.apphoctap.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    val c = Constraints

    /* Cung cấp một instance của SessionManager.
    Giúp lưu trữ Access Token, User Role, hoặc
    bất kỳ thông tin nào liên quan đến phiên làm việc của người dùng. */
    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }


    // Hoặc nếu dùng Dagger/Hilt
    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Provides
        @Singleton
        fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
            return NetworkMonitorImpl(context)
        }
    }


    /* Cung cấp một instance của AuthInterceptor để chèn
    Access Token vào tất cả các request gửi đến server.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptorHilt {
        return AuthInterceptorHilt(context)
    }

    // Cung cấp một OkHttpClient không có Interceptor (dành cho các API không cần xác thực).
    @Provides
    @Named("NoAuthClient")
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptorHilt): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    // Cung cấp một OkHttpClient có AuthInterceptor, giúp tự động thêm Access Token vào request.
    @Provides
    @Named("AuthClient")
    @Singleton
    fun provideOkHttpClientWithAuth(authInterceptor: AuthInterceptorHilt): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }


    // Cung cấp một instance của Retrofit để sử dụng trong toàn bộ ứng dụng.
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(c.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideRetrofitWithAuth(@Named("AuthClient") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(c.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("NoAuthRetrofit")
    fun provideRetrofitWithoutAuth(@Named("NoAuthClient") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(c.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    //Các hàm cung cấp API service từ Retrofit
    @Provides
    @Singleton
    fun provideUserApiService(@Named("AuthRetrofit") retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideAssignmentApiService(@Named("AuthRetrofit") retrofit: Retrofit): AssignmentApi =
        retrofit.create(AssignmentApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(@Named("AuthRetrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideClassesApiService(@Named("AuthRetrofit") retrofit: Retrofit): ClassApi =
        retrofit.create(ClassApi::class.java)

    @Provides
    @Singleton
    fun provideClassStudentApiService(@Named("AuthRetrofit") retrofit: Retrofit): ClassStudentApi =
        retrofit.create(ClassStudentApi::class.java)

    @Provides
    @Singleton
    fun provideFlashCardApiService(@Named("AuthRetrofit") retrofit: Retrofit): FlashCardApi =
        retrofit.create(FlashCardApi::class.java)

    @Provides
    @Singleton
    fun provideMessageApiService(@Named("AuthRetrofit") retrofit: Retrofit): MessageApi =
        retrofit.create(MessageApi::class.java)

    @Provides
    @Singleton
    fun provideStudentApiService(@Named("AuthRetrofit") retrofit: Retrofit): StudentApi =
        retrofit.create(StudentApi::class.java)

    @Provides
    @Singleton
    fun provideTeacherApiService(@Named("AuthRetrofit") retrofit: Retrofit): TeacherApi =
        retrofit.create(TeacherApi::class.java)

    @Provides
    @Singleton
    fun provideSubmissionApiService(@Named("AuthRetrofit") retrofit: Retrofit): SubmissionApi =
        retrofit.create(SubmissionApi::class.java)

    @Provides
    @Singleton
    fun provideVideoCallApiService(@Named("AuthRetrofit") retrofit: Retrofit): VideoCallApi =
        retrofit.create(VideoCallApi::class.java)

}

