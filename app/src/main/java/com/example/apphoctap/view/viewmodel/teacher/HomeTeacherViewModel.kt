package com.example.apphoctap.view.viewmodel.teacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.database.entities.ClassCacheEntitiy
import com.example.apphoctap.model.SumClassAndStudent
import com.example.apphoctap.network.RetrofitInstance
import com.example.apphoctap.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

import kotlinx.coroutines.launch

@HiltViewModel
class HomeTeacherViewModel @Inject constructor(private val sessionManager: SessionManager) : ViewModel() {

    private val repository = RetrofitInstance.classRepository


    private val _sum = MutableLiveData<SumClassAndStudent?>()
    val sum: LiveData<SumClassAndStudent?> = _sum

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun sumClassAndStudent(teacherID: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSumClassesByTeacherID(teacherID)
                if (response.isSuccessful) {
                    _sum.value = response.body()
                } else {
                    _error.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

        private val _classList = MutableLiveData<Result<List<ClassCacheEntitiy>>>()
    val classList: LiveData<Result<List<ClassCacheEntitiy>>> get() = _classList

    fun getClassesByTeacherID(teacherID: String) {
        viewModelScope.launch {
            try {
                val response = repository.getClassesByTeacherID(teacherID)
                if (response.isSuccessful) {
                    val result = response.body()?.map {
                        // Chuyển đổi ClassResponse → ClassCacheEntitiy nếu cần
                        ClassCacheEntitiy(
                            userId = sessionManager.getUserId()!!,
                            classId = it.classID,
                            className = it.className,
                            description = it.description,
                            teacherName = it.teacherName,
                            enrollmentKey = it.enrollmentKey,
                            lastSyncTime = System.currentTimeMillis()
                        )
                    } ?: emptyList()
                    _classList.value = Result.success(result)
                } else {
                    _classList.value = Result.failure(Exception("Lỗi: ${response.code()}"))
                }
            } catch (e: Exception) {
                _classList.value = Result.failure(e)
            }
        }
    }
}

