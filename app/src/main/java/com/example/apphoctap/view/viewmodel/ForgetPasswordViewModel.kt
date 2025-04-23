package com.example.apphoctap.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.Exposedforget
import com.example.apphoctap.model.ForgetState
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RetrofitInstance.authRepository
    private val _forgetState = MutableStateFlow<ForgetState>(ForgetState.Idle)
    val forgetState: StateFlow<ForgetState> = _forgetState

    fun sendEmail(Exposedemail: Exposedforget) {
        _forgetState.value = ForgetState.Loading
        viewModelScope.launch {
            try {
                val response = repository.sendForgotPassword(Exposedemail)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.message.isNotEmpty()) {
                        _forgetState.value = ForgetState.Success(body.message ?: "Đặt mật khẩu thành công")
                    } else {
                        _forgetState.value = ForgetState.Error("Phản hồi không hợp lệ từ server")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gửi email thất bại"
                    _forgetState.value = ForgetState.Error(errorMsg)
                }

            } catch (e: Exception) {
                _forgetState.value = ForgetState.Error("Lỗi kết nối: ${e.localizedMessage ?: "Không xác định"}")
            }
        }
    }

    fun resetState() {
        _forgetState.value = ForgetState.Idle
    }
}
