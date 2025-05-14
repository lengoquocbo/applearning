package com.example.apphoctap.view.document.filemanagement

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.FileItem
import com.example.apphoctap.repository.FileRepository
import com.example.apphoctap.utils.FileResult
import com.example.apphoctap.utils.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FileViewModel @Inject constructor (private val fileRepository: FileRepository) : ViewModel(){

    private val _filesResult = MutableLiveData<FileResult<List<FileItem>>>()
    val filesResult: LiveData<FileResult<List<FileItem>>> = _filesResult

    private val _uploadState = MutableLiveData<UploadState>(UploadState.Idle)
    val uploadState: LiveData<UploadState> = _uploadState

    private val _downloadUrl = MutableLiveData<Pair<String, String>>() // Pair<url, filename>
    val downloadUrl: LiveData<Pair<String, String>> = _downloadUrl

    fun getFiles() {
        viewModelScope.launch {
            _filesResult.value = FileResult.Loading
            val result = fileRepository.getFiles()
            _filesResult.value = result
        }
    }

    fun uploadFiles(uris: List<Uri>) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val result =
                fileRepository.uploadMultipleFiles(uris, classId = "personal", fileType = "PERSONAL")
            if (!result.isNullOrEmpty()) {
                val fileIds = result.map { it.fileId }
                _uploadState.value = UploadState.Success(fileIds)
            } else {
                _uploadState.value = UploadState.Error("File upload failed")
            }
        }
    }


    fun fetchDownloadUrl(fileId: String, fileName: String) {
        viewModelScope.launch {
            try {
                val url = fileRepository.getDownloadUrl(fileId)
                _downloadUrl.postValue(url to fileName)
            } catch (e: Exception) {
                Log.e("AssignmentViewModel", "Error fetching download URL", e)
            }
        }
    }

}