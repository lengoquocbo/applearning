package com.example.apphoctap.view.classdetail.assignment

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.AssignmentRequest
import com.example.apphoctap.model.AssignmentSubmission
import com.example.apphoctap.model.SubmitRequest
import com.example.apphoctap.model.UpdateAssignmentRequest
import com.example.apphoctap.network.api.DeleteResponse
import com.example.apphoctap.network.api.UpdateResponse
import com.example.apphoctap.repository.AssignmentRepository
import com.example.apphoctap.repository.FileRepository
import com.example.apphoctap.repository.SubmissionRepository
import com.example.apphoctap.utils.AssignmentCreationState
import com.example.apphoctap.utils.AssignmentState
import com.example.apphoctap.utils.ResultAssignment
import com.example.apphoctap.utils.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val assignmentRepository : AssignmentRepository,
    private val submissionRepository: SubmissionRepository,
    private val fileRepository : FileRepository
) :ViewModel() {

    private val _creationState = MutableLiveData<AssignmentCreationState>(AssignmentCreationState.Idle)
    val creationState: LiveData<AssignmentCreationState> = _creationState

    private val _uploadState = MutableLiveData<UploadState>(UploadState.Idle)
    val uploadState: LiveData<UploadState> = _uploadState

    private val _assignmentState = MutableLiveData<AssignmentState>(AssignmentState.Idle)
    val assignmentState: LiveData<AssignmentState> = _assignmentState

    private val _downloadUrl = MutableLiveData<Pair<String, String>>() // Pair<url, filename>
    val downloadUrl: LiveData<Pair<String, String>> = _downloadUrl

    private val _assignments = MutableLiveData<ResultAssignment<List<AssignmentSubmission>>>()
    val assignments: MutableLiveData<ResultAssignment<List<AssignmentSubmission>>> = _assignments

    private val _submission = MutableLiveData<AssignmentState>(AssignmentState.Idle)
    val submission: LiveData<AssignmentState> = _submission

    private val _deleteAssignmentResult = MutableLiveData<ResultAssignment<DeleteResponse>>()
    val deleteAssignmentResult: LiveData<ResultAssignment<DeleteResponse>> = _deleteAssignmentResult

    private val _updateAssignment = MutableLiveData<ResultAssignment<UpdateResponse>>()
    val updateAssignment: LiveData<ResultAssignment<UpdateResponse>> = _updateAssignment

    fun updateAssignment(assignmentId : Int, title: String, description: String, dueDate: String) {
        viewModelScope.launch {
            val updateAssignmentRequest = UpdateAssignmentRequest(
                title,
                description,
                dueDate
            )
            _updateAssignment.value = ResultAssignment.Loading
            val result = assignmentRepository.updateAssignment(assignmentId, updateAssignmentRequest)
            _updateAssignment.value = result
        }
    }

    fun deleteAssignment(assignmentId: Int) {
        viewModelScope.launch {
            _deleteAssignmentResult.value = ResultAssignment.Loading
            val result = assignmentRepository.deleteAssignment(assignmentId)
            _deleteAssignmentResult.value = result
        }
    }

    fun uploadFilesAndSubmitAssignment(
        uris: List<Uri>,
        assignment: AssignmentSubmission,
        classId : String
    ){
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val fileType = "SUBMISSION"
            val uploadResults = fileRepository.uploadMultipleFiles(uris, classId, fileType)
            if (!uploadResults.isNullOrEmpty()) {
                Log.d("UploadState success", "Upload thành công")
                val fileIds = uploadResults.map { it.fileId }
                val attachmentIdsString = fileIds.joinToString(",")

                _uploadState.value = UploadState.Success(fileIds)

                _submission.value = AssignmentState.Creating
                try {
                    val request = SubmitRequest(
                        assignmentId = assignment.id,
                        fileSub = attachmentIdsString,
                        dueDate = assignment.dueDate
                    )

                    try {
                        val result = submissionRepository.createSubmission(request)
                        when (result) {
                            is ResultAssignment.Success -> {
                                // Xử lý khi thành công, result.data đã chứa message từ server
                                val message = result.data
                                _submission.value = AssignmentState.Created(result.data)
                            }

                            is ResultAssignment.Error -> {
                                // Xử lý khi có lỗi
                                val errorMsg = result.message
                                Log.d("submissionRepository", errorMsg)
                            }

                            is ResultAssignment.Loading -> {
                            }

                        }
                    } catch (e: Exception) {
                        // Xử lý ngoại lệ không mong muốn (thông thường repository đã xử lý exceptions)
                    }
                } catch (e: Exception) {
                    _assignmentState.value =
                        AssignmentState.Error(e.message ?: "Exception occurred")
                }
            } else {
                Log.d("uploadState error", "Upload không thành công")
                _uploadState.value = UploadState.Error("File upload failed")
            }
        }
    }

    fun getAssignments(classId: String) {
        viewModelScope.launch {
            _assignments.value = ResultAssignment.Loading
            val result = assignmentRepository.getAssignmentsByClassId(classId)
            _assignments.value = result
        }
    }

    fun uploadFilesAndCreateAssignment(
        uris: List<Uri>,
        classId: String?,
        title: String,
        description: String,
        dueDate : String
    ) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val fileType = "ASSIGNMENT"
            val uploadResults = fileRepository.uploadMultipleFiles(uris, classId, fileType)

            if (!uploadResults.isNullOrEmpty()) {
                Log.d("UploadState success", "Upload thành công")
                val fileIds = uploadResults.map { it.fileId }
                val attachmentIdsString = fileIds.joinToString(",")
                _uploadState.value = UploadState.Success(fileIds)

                // Sau khi upload thành công → tạo assignment
                _assignmentState.value = AssignmentState.Creating
                try {
                    val request = AssignmentRequest(
                        classId = classId?:"",
                        title = title,
                        description = description,
                        dueDate  = dueDate,
                        attachmentIds = attachmentIdsString
                    )

                    try {
                        val result = assignmentRepository.createAssignment(request)
                        when (result) {
                            is ResultAssignment.Success -> {
                                // Xử lý khi thành công, result.data đã chứa message từ server
                                val message = result.data
                                _assignmentState.value = AssignmentState.Created(result.data)

                            }
                            is ResultAssignment.Error -> {
                                // Xử lý khi có lỗi
                                val errorMsg = result.message
                                _assignmentState.value = AssignmentState.Error(errorMsg)
                            }
                            is ResultAssignment.Loading -> {
                                // Thông thường không cần xử lý ở đây vì Loading đã được xử lý trước khi gọi repository
                            }
                        }
                    } catch (e: Exception) {
                        // Xử lý ngoại lệ không mong muốn (thông thường repository đã xử lý exceptions)
                    }
                } catch (e: Exception) {
                    _assignmentState.value = AssignmentState.Error(e.message ?: "Exception occurred")
                }

            } else {
                Log.d("uploadState error", "Upload không thành công")
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

