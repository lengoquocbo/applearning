package com.example.apphoctap.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ExposedUser
import com.example.apphoctap.model.ExposedUserLogin
import com.example.apphoctap.model.LoginResponse
import com.example.apphoctap.model.LoginState
import com.example.apphoctap.network.RetrofitInstance
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val repository = RetrofitInstance.authRepository
    private val loginState = MutableStateFlow<LoginState>(com.example.apphoctap.model.LoginState.Idle)
    val LoginState: StateFlow<LoginState> = loginState

    fun login(userLogin: ExposedUserLogin) {
        loginState.value = com.example.apphoctap.model.LoginState.Loading

        viewModelScope.launch {
            try {
                val response = repository.login(userLogin)

                if (response.isSuccessful) {
                    val body: LoginResponse? = response.body()

                    if (body != null && body.token.isNotBlank() && body.refreshToken.isNotBlank()) {
                        val payload = JwtUtils.decodeJwt(body.token)

                        if (payload != null) {
                            val userId = payload.optString("userID", "")
                            val email = payload.optString("email", "")
                            val role = body.role

                            val user = ExposedUser(
                                userID = userId,
                                username = email.substringBefore("@"),
                                email = email,
                                password = "",
                                role = role,
                                sdt = ""
                            )

                            sessionManager.saveUserSession(body.token, body.refreshToken, role)

                            loginState.value = com.example.apphoctap.model.LoginState.Success(
                                token = body.token,
                                refreshToken = body.refreshToken,
                                user = user,
                                message = "Đăng nhập thành công"
                            )
                            println(body.token)
                            println(body.refreshToken)
                            println(user.userID)
                            println(user.email)
                            println(user.role)



                        } else {
                            loginState.value = com.example.apphoctap.model.LoginState.Error("Giải mã token thất bại")
                        }
                    } else {
                        loginState.value = com.example.apphoctap.model.LoginState.Error("Phản hồi không hợp lệ từ server")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Đăng nhập thất bại"
                    loginState.value = com.example.apphoctap.model.LoginState.Error(errorMsg)
                }

            } catch (e: Exception) {
                loginState.value = com.example.apphoctap.model.LoginState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
        loginState.value = com.example.apphoctap.model.LoginState.Idle
    }

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLoggedIn()
    }
}
