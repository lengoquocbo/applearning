package com.example.apphoctap.network

import android.content.Context
import com.example.apphoctap.network.api.AssignmentApi
import com.example.apphoctap.network.api.AuthApi
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.network.api.ClassesApi
import com.example.apphoctap.network.api.FlashCardApi
import com.example.apphoctap.network.api.MessageApi
import com.example.apphoctap.network.api.MinitestAnswerApi
import com.example.apphoctap.network.api.MinitestApi
import com.example.apphoctap.network.api.MinitestQuestionApi
import com.example.apphoctap.network.api.MinitestResultApi
import com.example.apphoctap.network.api.StudentAnswerApi
import com.example.apphoctap.network.api.StudentApi
import com.example.apphoctap.network.api.SubmissionApi
import com.example.apphoctap.network.api.TeacherApi
import com.example.apphoctap.network.api.UserApi
import com.example.apphoctap.network.api.VideoCallApi
import com.example.apphoctap.utils.Constraints
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

    /* Cung cấp một instance của AuthInterceptor để chèn
    Access Token vào tất cả các request gửi đến server.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    // Cung cấp một OkHttpClient không có Interceptor (dành cho các API không cần xác thực).
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    // Cung cấp một OkHttpClient có AuthInterceptor, giúp tự động thêm Access Token vào request.
    @Provides
    @Singleton
    fun provideOkHttpClientWithAuth(authInterceptor: AuthInterceptor): OkHttpClient {
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


    //Các hàm cung cấp API service từ Retrofit
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAssignmentApiService(retrofit: Retrofit) : AssignmentApi{
        return retrofit.create(AssignmentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClassesApiService(retrofit: Retrofit) : ClassesApi {
        return retrofit.create(ClassesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClassStudentApiService(retrofit: Retrofit): ClassStudentApi {
        return retrofit.create(ClassStudentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFlashCardApiService(retrofit: Retrofit) : FlashCardApi {
        return retrofit.create(FlashCardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageApiService(retrofit: Retrofit) : MessageApi {
        return retrofit.create(MessageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMinitestAnswerApiService(retrofit: Retrofit) : MinitestAnswerApi {
        return retrofit.create(MinitestAnswerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMiniTestApiService(retrofit: Retrofit) : MinitestApi {
        return retrofit.create(MinitestApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMinitestQuestionApiService(retrofit: Retrofit) : MinitestQuestionApi {
        return retrofit.create(MinitestQuestionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMinitestResultApiService(retrofit: Retrofit) : MinitestResultApi {
        return retrofit.create(MinitestResultApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStudentAnswerApiService(retrofit: Retrofit) : StudentAnswerApi {
        return retrofit.create(StudentAnswerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStudentApiService(retrofit: Retrofit) : StudentApi {
        return retrofit.create(StudentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTeacherApiService(retrofit: Retrofit) : TeacherApi {
        return retrofit.create(TeacherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSubmissionApiService(retrofit: Retrofit) : SubmissionApi {
        return retrofit.create(SubmissionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoCallApiService(retrofit: Retrofit) : VideoCallApi {
        return retrofit.create(VideoCallApi::class.java)
    }

}
