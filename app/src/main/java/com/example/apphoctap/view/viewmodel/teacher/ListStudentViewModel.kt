package com.example.apphoctap.view.viewmodel.teacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.AddStudentRequest
import com.example.apphoctap.model.StudentResponse
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.launch

class ListStudentViewModel : ViewModel() {
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
    fun deleteStudent(studentID: String) {
        viewModelScope.launch {
            try {
                val response = repositorystudent.deleteStudentByStudentID(studentID)
                if (response.isSuccessful) {
                    val message = response.body() ?: "Xóa học sinh thành công"
                    _deleteResult.postValue(Result.success(message))

                } else {
                    _deleteResult.postValue(Result.failure(Exception("Lỗi ${response}")))
                }
            } catch (e: Exception) {
                _deleteResult.postValue(Result.failure(e))
            }
        }
    }
    fun addStudentByEmail(addStudentRequest: AddStudentRequest) {
        viewModelScope.launch {
            try {
                val response = repositorystudent.addStudentByEmail(addStudentRequest)
                if (response.isSuccessful) {
                    val message = response.body() ?: "Thêm học sinh thành công"
                    _studentList.postValue(Result.success(_studentList.value?.getOrNull() ?: listOf()))
                    getStudentByClassID(addStudentRequest.classID)

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
