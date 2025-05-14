package com.example.apphoctap.network.api

import com.example.apphoctap.model.ClassMaterial
import com.example.apphoctap.model.FileItem
import com.example.apphoctap.model.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.*

interface FileApi {
    @Multipart
    @POST("file/{classId}")
    suspend fun uploadFile(
        @Path("classId") classId: String,
        @Part file: MultipartBody.Part
    ): Response<FileUploadResponse>

    @Multipart
    @POST("file/multiple/{classId}/{fileType}")
    suspend fun uploadFiles(
        @Path("classId") classId: String,
        @Path("fileType") fileType: String,
        @Part files: List<MultipartBody.Part>
    ): Response<FileIdListResponse>


    @GET("file/download-link")
    suspend fun getDownloadUrl(@Query("fileId") fileId: String): DownloadUrlResponse

    @GET("file/materials")
    suspend fun getMaterials(@Query("classId") classId: String): Response<List<ClassMaterial>>

    @GET("file/user")
    suspend fun getFiles() : Response<List<FileItem>>
}


data class FileIdListResponse(val fileIds: List<Int>)

data class DownloadUrlResponse(val url: String)



