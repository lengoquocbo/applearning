package com.example.apphoctap.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.apphoctap.model.ClassMaterial
import com.example.apphoctap.model.FileItem
import com.example.apphoctap.model.FileUploadResponse
import com.example.apphoctap.network.api.FileApi
import com.example.apphoctap.utils.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class FileRepository(
    private val fileApi: FileApi,
    private val context: Context
) {

    suspend fun uploadSingleFile(uri: Uri, classId: String?): FileUploadResponse? {
        val part = createMultipartFromUri(uri) ?: return null
        return try {
            val response = fileApi.uploadFile(classId ?: "unknown", part)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun uploadMultipleFiles(uris: List<Uri>, classId: String?, fileType : String): List<FileUploadResponse>? {
        val parts = uris.mapNotNull { createMultipartFromUri(it) }
        return try {
            val response = fileApi.uploadFiles(classId ?: "unknown", fileType, parts)
            if (response.isSuccessful) {
                response.body()?.fileIds?.map { FileUploadResponse(it) }  // nếu bạn cần đúng định dạng cũ
            } else {
                Log.e("Upload", "Upload failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createMultipartFromUri(uri: Uri): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val fileName = getFileNameFromUri(context, uri)
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

        val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", fileName, requestBody)
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) result = cursor.getString(index)
                }
            }
        }
        return result ?: uri.lastPathSegment ?: "unknown_file"
    }

    suspend fun getDownloadUrl(fileId: String): String {
        val response = fileApi.getDownloadUrl(fileId)
        return response.url
    }


    suspend fun getMaterials(classId : String) : List<ClassMaterial>{
        var materials = listOf<ClassMaterial>()
        withContext(Dispatchers.IO) {
            val response = fileApi.getMaterials(classId)
            if (response.isSuccessful) {
                Log.d("getMaterials", "Success: ${response.body()}")
                materials = response.body()!!
                return@withContext materials

                // Xử lý danh sách materials ở đây
            } else {
                Log.d("getMaterials", "Failed to fetch materials: ${response.code()}")
                return@withContext emptyList()

            }
        }
        return materials
    }

    suspend fun getFiles(): FileResult<List<FileItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = fileApi.getFiles()
                if (response.isSuccessful) {
                    response.body()?.let { files ->
                        if (files.isEmpty()) {
                            FileResult.Empty
                        } else {
                            FileResult.Success(files)
                        }
                    } ?: FileResult.Error("Response body is null")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    FileResult.Error("Failed to get files: $errorBody")
                }
            } catch (e: Exception) {
                FileResult.Error("Exception while getting files: ${e.message}")
            }
        }
    }


}

