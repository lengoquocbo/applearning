package com.example.apphoctap.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ExposeNewPass
import com.example.apphoctap.model.ForgetState
import com.example.apphoctap.model.NewPassState
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {
    private val repo = RetrofitInstance.authRepository

    private val _state = MutableStateFlow<NewPassState>(NewPassState.Idle)
    val state: StateFlow<NewPassState> get() = _state

    fun resetPassword(ExposeNewPass: ExposeNewPass) {
        viewModelScope.launch {
            _state.value = NewPassState.Loading
            try {
                val response = repo.resetPassword(ExposeNewPass)
                if (response.isSuccessful ) {
                    val body = response.body()
                    if (body != null && body.message.isNotEmpty()) {
                        _state.value = NewPassState.Success(body.message)
                    }
                } else {
                    _state.value = NewPassState.Error( "Đặt mật khẩu không thành công")
                }
            } catch (e: Exception) {
                _state.value = NewPassState.Error("Lỗi hệ thống: ${e.localizedMessage}")
            }
        }
    }
}
