package com.example.apphoctap.view.viewmodel.teacher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.CreateClassRequest
import com.example.apphoctap.network.RetrofitInstance
import com.example.apphoctap.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateClassViewModel @Inject constructor(
    private val chatClient: ChatClient,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val repository = RetrofitInstance.classRepository

    private val _createClassResult = MutableLiveData<Result<String>>()
    val createClassResult: LiveData<Result<String>> get() = _createClassResult

    fun createClass(
        classID: String,
        teacherID: String,
        className: String,
        description: String,
        enrollmentKey: String
    ) {
        val createAt = getCurrentDateTime()

        val newClass = CreateClassRequest(
            classID = classID,
            teacherID = teacherID,
            className = className,
            description = description,
            createAt = createAt,
            enrollmentKey = enrollmentKey
        )

        viewModelScope.launch {
            try {
                val response = repository.createClass(newClass)
                if (response.isSuccessful) {
                    val body = response.body()
                    _createClassResult.value = Result.success(body?.message ?: "Tạo lớp thành công")

                    val memberIds = sessionManager.getUserId().toString()
                    chatClient.createChannel(
                        channelType = "messaging",
                        channelId = enrollmentKey,
                        memberIds = listOf(memberIds),
                        extraData = mutableMapOf(
                            "name" to className,
                            "image" to ""
                        )
                    ).enqueue { result ->
                        if (result is io.getstream.result.Result.Success) {
                            val channel = result.value
                            Log.d("Success when create channel", "Success : ${channel.name}")
                        } else if (result is io.getstream.result.Result.Failure){
                            val error = result.value
                            Log.e("ChannelCreation", "Failed to create channel: ${error.message}, cause: $error")
                        }
                    }
                } else {
                    _createClassResult.value = Result.failure(Exception("Lỗi: ${response.code()}"))
                }
            } catch (e: Exception) {
                _createClassResult.value = Result.failure(e)
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
