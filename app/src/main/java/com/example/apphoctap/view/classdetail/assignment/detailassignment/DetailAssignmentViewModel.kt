package com.example.apphoctap.view.classdetail.assignment.detailassignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.SubmissionsResponse
import com.example.apphoctap.repository.FileRepository
import com.example.apphoctap.repository.SubmissionRepository
import com.example.apphoctap.utils.ResultAssignment
import com.example.apphoctap.view.classdetail.assignment.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailAssignmentViewModel @Inject constructor(
    private val submissionRepository: SubmissionRepository,
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _assignmentDetail = MutableLiveData<ResultAssignment<List<SubmissionsResponse>>>()
    val assignmentDetail: MutableLiveData<ResultAssignment<List<SubmissionsResponse>>> = _assignmentDetail

    private val _downloadUrl = MutableLiveData<Event<Pair<String, String>>>()
    val downloadUrl: LiveData<Event<Pair<String, String>>> = _downloadUrl

    private val _feedbackSent = MutableLiveData<Boolean>()
    val feedbackSent: LiveData<Boolean> = _feedbackSent

    fun sendFeedback(submissionId: Int, feedback: String) {
        viewModelScope.launch {
            try {
                submissionRepository.sendFeedback(submissionId, feedback)
                _feedbackSent.value = true
            } catch (e: Exception) {
                _feedbackSent.value = false
            }
        }
    }

    fun getDetailAssignment(assignmentId : Int) {
        viewModelScope.launch {
            _assignmentDetail.value = ResultAssignment.Loading
            val result = submissionRepository.getSubmissionsByAssignmentId(assignmentId)
            _assignmentDetail.value = result
        }
    }

    fun fetchDownloadUrl(fileId: String, fileName: String) {
        viewModelScope.launch {
            try {
                val url = fileRepository.getDownloadUrl(fileId)
                _downloadUrl.postValue(Event(url to fileName))
            } catch (e: Exception) {
                Log.e("AssignmentViewModel", "Error fetching download URL", e)
            }
        }
    }
}