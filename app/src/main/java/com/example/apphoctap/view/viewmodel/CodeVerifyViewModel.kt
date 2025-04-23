package com.example.apphoctap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.CodeState
import com.example.apphoctap.model.ExposedCode
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CodeVerifyViewModel : ViewModel() {

    private val repo = RetrofitInstance.authRepository

    private val _state = MutableStateFlow<CodeState>(CodeState.Idle)
    val state: StateFlow<CodeState> = _state

    fun verifyCode(ExposedCode: ExposedCode) {
        viewModelScope.launch {
            _state.value = CodeState.Loading
            try {
                val response = repo.verifyCode(ExposedCode)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _state.value = CodeState.Success(body.message ?: "Xác minh thành công")
                    }
                } else {
                    _state.value = CodeState.Error("Mã không đúng hoặc hết hạn")
                }
            } catch (e: Exception) {
                _state.value = CodeState.Error("Lỗi kết nối: ${e.localizedMessage}")
            }
        }
    }
    fun resetState() {
        _state.value = CodeState.Idle
    }
}
