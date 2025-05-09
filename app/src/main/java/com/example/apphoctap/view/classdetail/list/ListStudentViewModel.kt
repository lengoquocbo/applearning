package com.example.apphoctap.view.classdetail.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.AddStudentRequest
import com.example.apphoctap.model.DeleteStudentRequest
import com.example.apphoctap.model.StudentResponse
import com.example.apphoctap.network.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ListStudentViewModel @Inject constructor(val client: ChatClient) : ViewModel() {
    private val repository = RetrofitInstance.classRepository
    private val repositorystudent = RetrofitInstance.studentRepositoy

    private val _studentList = MutableLiveData<Result<List<StudentResponse>>>()
    val studentList: LiveData<Result<List<StudentResponse>>> get() = _studentList

    private val _deleteResult = MutableLiveData<Result<String>>()
    val deleteResult: LiveData<Result<String>> get() = _deleteResult

    private val _addResult = MutableLiveData<Result<String>>()
    val addResult: LiveData<Result<String>> get() = _addResult

    fun getStudentByClassID(classID: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStudentByclassID(classID)
                if (response.isSuccessful) {
                    val result = response.body()?.map {
                        StudentResponse(
                            classID = it.classID,
                            studentID = it.studentID,
                            studentname = it.studentname
                        )
                    } ?: emptyList()
                    _studentList.postValue(Result.success(result))
                } else {
                    _studentList.postValue(Result.failure(Exception("Lỗi API: ${response.code()}")))
                }
            } catch (e: Exception) {
                _studentList.postValue(Result.failure(e))
            }
        }
    }
    fun deleteStudent(deleteStudentRequest: DeleteStudentRequest) {
        viewModelScope.launch {
            try {
                val response = repositorystudent.deleteStudentByStudentID(deleteStudentRequest)
                if (response.isSuccessful) {

                    val userId = response.body()!!.userId
                    val enrollmentKey = response.body()!!.enrollmentKey
                    val channelClient = client.channel("messaging", enrollmentKey)
                    channelClient.removeMembers(listOf(userId)).enqueue { result ->
                        if (result is io.getstream.result.Result.Success) {
                            val channel: Channel = result.value

                            Log.d("Delete member", "Học sinh hiện có ${channel.members}")
                        } else {
                            Log.d("Delete member", "Lỗi khi xóa member: ${result.isFailure}")
                        }
                    }
                    val message = response.body()!!.message
                    _deleteResult.postValue(Result.success(message))
                    // Remove member with id "tommaso"

                    _deleteResult.postValue(Result.success(message))

                } else {
                    _deleteResult.postValue(Result.failure(Exception("Lỗi ${response}")))
                    Log.e("Delete Student", "Error deleting student 123 : ${response}")

                }
            } catch (e: Exception) {
                _deleteResult.postValue(Result.failure(e))
                Log.e("Delete Student", "Error deleting student: ${e}")
            }
        }
    }
    fun addStudentByEmail(addStudentRequest: AddStudentRequest) {
        viewModelScope.launch {
            try {
                val response = repositorystudent.addStudentByEmail(addStudentRequest)
                if (response.isSuccessful) {
                    val message = response.body() ?: "Thêm học sinh thành công"
                    Log.d("List Channel", "Channel created successfully: ${message}")
                    _studentList.postValue(Result.success(_studentList.value?.getOrNull() ?: listOf()))

                    val enrollMentKey = response.body()!!.enrollmentKey
                    if(enrollMentKey == "") return@launch

                    withContext(Dispatchers.IO) {
                        getStudentByClassID(addStudentRequest.classID)
                    }

                    val channelClient = client.channel("messaging", channelId = enrollMentKey)
                    val userId = response.body()!!.userId
                    channelClient.addMembers(listOf(userId)).enqueue { result ->
                        if (result is io.getstream.result.Result.Success) {
                            val channel: Channel = result.value
                            Log.d("List Channel", "Channel created successfully: ${channel.name}")
                        } else {
                            Log.d("List Channel", "Channel creation failed: ${result.isFailure}")
                        }
                    }
                    // Hoặc reload danh sách học sinh nếu cần thiết
                } else {
                    _studentList.postValue(Result.failure(Exception("Lỗi ${response}")))
                }
            } catch (e: Exception) {
                _studentList.postValue(Result.failure(e))
            }
        }
    }



}
