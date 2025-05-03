package com.example.apphoctap.view.student.myclass.myclassmanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ClassUiModel
import com.example.apphoctap.repository.ClassRepository
import com.example.apphoctap.utils.AccessDeniedError
import com.example.apphoctap.utils.InvalidEnrollmentKeyError
import com.example.apphoctap.utils.JoinClassState
import com.example.apphoctap.utils.NetworkError
import com.example.apphoctap.utils.ResultAction
import com.example.apphoctap.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassViewModel @Inject constructor(private val classRepository: ClassRepository) : ViewModel() {

    private val _classes = MutableLiveData<UiState<List<ClassUiModel>>>(UiState.Loading)
    val classes: LiveData<UiState<List<ClassUiModel>>> = _classes

    private val _joinClassState = MutableLiveData<JoinClassState>(JoinClassState.Idle)
    val joinClassState: LiveData<JoinClassState> = _joinClassState

    //Quan sát trạng thái của 1 thao tác
    private val _operationStatus = MutableLiveData<UiState<String>>()
    val operationStatus: LiveData<UiState<String>> = _operationStatus

    init {
        loadClasses()
    }

    fun loadClasses() {
        viewModelScope.launch {
            _classes.value = UiState.Loading
            try {
                val result = classRepository.getClasses()
                _classes.value = UiState.Success(result)
            } catch (e: Exception) {
                _classes.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteClass(classId: String, studentId : String) {
        viewModelScope.launch {
            _operationStatus.value = UiState.Loading

            when(val result = classRepository.leaveClass(classId, studentId)) {
                is UiState.Success -> {
                    _operationStatus.value = UiState.Success("Class deleted successfully")
                    loadClasses()
                }
                is UiState.Error -> {
                    _operationStatus.value = UiState.Error(result.message ?: "Failed to delete class")
                }
                else -> {
                }
            }
        }
    }

    fun joinClass(enrollmentKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _joinClassState.value = JoinClassState.Loading

            when (val result = classRepository.joinClassByEnrollmentKey(enrollmentKey)) {
                is ResultAction.Success -> {
                    _joinClassState.value = JoinClassState.Success(result.data)
                }
                is ResultAction.Error -> {
                    _joinClassState.value = JoinClassState.Error("Không thể tham gia lớp học. Vui lòng kiểm tra lại mã tham gia.")
                }
                is ResultAction.Loading -> {
                    //chưa biết nên xử lý như thế nào
                }
            }
        }
    }


}