package com.example.apphoctap.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.apphoctap.network.api.FileApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class FileUploader(
    private val context: Context,
    private val fileApi: FileApi
) {

    suspend fun upload(uri: Uri, classId: String): Int? {
        val part = createMultipartFromUri(uri) ?: return null

        return try {
            val response = fileApi.uploadFile(classId, part)
            if (response.isSuccessful) {
                response.body()?.fileId
            } else null
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

        val requestBody = inputStream.readBytes()
            .toRequestBody(mimeType.toMediaTypeOrNull())
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
}
