package com.example.apphoctap.view.viewmodel.teacher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.network.RetrofitInstance
import com.example.apphoctap.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val repository = RetrofitInstance.authRepository

    private val _logoutState = MutableLiveData<Result<Boolean>>()
    val logoutState: LiveData<Result<Boolean>> = _logoutState

    // Đăng xuất người dùng
    fun logout() {
        viewModelScope.launch {
            try {
                // Nếu server hỗ trợ logout API, gọi logout API ở đây
                // val response = repository.logout()

                // Nếu không cần gọi API, chỉ cần xóa token
                sessionManager.clearToken()

                // Cập nhật trạng thái logout thành công
                _logoutState.value = Result.success(true)

            } catch (e: Exception) {
                _logoutState.value = Result.failure(e)
                Log.e("ProfileViewModel", "Error logging out", e)
            }
        }
    }
}
