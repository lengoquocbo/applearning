package com.example.apphoctap.view.viewmodel.teacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.database.entities.ClassCacheEntitiy
import com.example.apphoctap.network.RetrofitInstance

import kotlinx.coroutines.launch


class HomeTeacherViewModel : ViewModel() {

    private val repository = RetrofitInstance.classRepository

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

