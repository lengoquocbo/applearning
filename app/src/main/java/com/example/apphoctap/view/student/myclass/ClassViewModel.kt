package com.example.apphoctap.view.student.myclass

import LeaveClassState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ClassUiModel
import com.example.apphoctap.repository.ClassRepository
import com.example.apphoctap.utils.AccessDeniedError
import com.example.apphoctap.utils.ApiError
import com.example.apphoctap.utils.JoinClassState
import com.example.apphoctap.utils.NetworkError
import com.example.apphoctap.utils.NotFoundError
import com.example.apphoctap.utils.ResultAction
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.utils.UiState
import com.example.apphoctap.utils.UnauthorizedError
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClassViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _classes = MutableLiveData<UiState<List<ClassUiModel>>>(UiState.Loading)
    val classes: LiveData<UiState<List<ClassUiModel>>> = _classes

    private val _joinClassState = MutableLiveData<JoinClassState>(JoinClassState.Idle)
    val joinClassState: LiveData<JoinClassState> = _joinClassState

    //Quan sát trạng thái của 1 thao tác
    private val _operationStatus = MutableLiveData<UiState<String>>()
    val operationStatus: LiveData<UiState<String>> = _operationStatus

    private val _leaveClassState = MutableLiveData<LeaveClassState>()
    val leaveClassState: LiveData<LeaveClassState> = _leaveClassState

    init {
        loadClasses()
    }

    fun loadClasses() {
        Log.d("token", "token của học sinh ${sessionManager.getAccessToken()}")
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _classes.value = UiState.Loading
                delay(500)
            }
            try {
                val result = classRepository.getClasses()
                Log.d("ClassViewModel", "Loaded classes: $result")
                withContext(Dispatchers.Main) {
                    _classes.value = UiState.Success(result)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _classes.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }


    fun leaveClass(classId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _leaveClassState.value = LeaveClassState.Loading
            }

            when (val result = classRepository.leaveClass(classId)) {
                is ResultAction.Success -> {
                    withContext(Dispatchers.Main) {
                        _leaveClassState.value = LeaveClassState.Success
                    }
                }
                is ResultAction.Error -> {
                    withContext(Dispatchers.Main) {
                        val message = when (result.error) {
                            is UnauthorizedError -> "Vui lòng đăng nhập lại."
                            is AccessDeniedError -> "Bạn không có quyền rời lớp này."
                            is NotFoundError -> "Không tìm thấy lớp học hoặc học sinh."
                            is NetworkError -> "Lỗi kết nối mạng: ${result.error.message}"
                            is ApiError -> result.error.message
                            else -> "Không thể rời lớp học. Vui lòng thử lại."
                        }
                        _leaveClassState.value = LeaveClassState.Error(message)
                    }
                }
                is ResultAction.Loading -> {}
            }
        }
    }

    fun onClassClicked(classId: String) {
        viewModelScope.launch {
            classRepository.updateClassAccessTime(classId)
        }
    }


    fun joinClass(enrollmentKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _joinClassState.value = JoinClassState.Loading
            }

            when (val result = classRepository.joinClassByEnrollmentKey(enrollmentKey)) {
                is ResultAction.Success -> {
                    withContext(Dispatchers.Main) {
                        _joinClassState.value = JoinClassState.Success(result.data)
                    }
                }
                is ResultAction.Error -> {
                    withContext(Dispatchers.Main) {
                        _joinClassState.value =
                            JoinClassState.Error("Không thể tham gia lớp học. Vui lòng kiểm tra lại mã tham gia.")
                    }
                }
                is ResultAction.Loading -> {
                    //chưa biết nên xử lý như thế nào
                }
            }
        }
    }
}