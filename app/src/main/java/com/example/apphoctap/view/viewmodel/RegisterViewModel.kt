package com.example.apphoctap.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ExposedUser
import com.example.apphoctap.model.RegisterState
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    // Lấy AuthRepository trực tiếp từ RetrofitInstance
    private val repository = RetrofitInstance.authRepository

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(user: ExposedUser) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val response = repository.Registers(user)
                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success("Đăng ký thành công")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Đăng ký thất bại"
                    _registerState.value = RegisterState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }
}
