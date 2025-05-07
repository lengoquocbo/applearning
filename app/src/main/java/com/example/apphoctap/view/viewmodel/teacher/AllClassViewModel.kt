package com.example.apphoctap.view.viewmodel.teacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.database.entities.ClassCacheEntitiy
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.launch

class AllClassViewModel : ViewModel() {
    private val repository = RetrofitInstance.classRepository

    private val _classList = MutableLiveData<Result<List<ClassCacheEntitiy>>>()
    val classList: LiveData<Result<List<ClassCacheEntitiy>>> get() = _classList

    private val _deleteResult = MutableLiveData<Result<String>>()
    val deleteResult: LiveData<Result<String>> get() = _deleteResult

    fun getClassesByTeacherID(teacherID: String) {
        viewModelScope.launch {
            try {
                val response = repository.getClassesByTeacherID(teacherID)
                if (response.isSuccessful) {
                    val result = response.body()?.map {
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

    fun deleteClass(classID: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteClassTeacher(classID)
                if (response.isSuccessful) {
                    _deleteResult.postValue(Result.success("Xóa thành công"))
                } else {
                    _deleteResult.postValue(Result.failure(Exception("Xóa thất bại: ${response.code()}")))
                }
            } catch (e: Exception) {
                _deleteResult.postValue(Result.failure(e))
            }
        }
    }
}
