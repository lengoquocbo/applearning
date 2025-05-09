package com.example.apphoctap.view.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ExposedUser
import com.example.apphoctap.model.ExposedUserLogin
import com.example.apphoctap.model.LoginResponse
import com.example.apphoctap.model.LoginState
import com.example.apphoctap.network.NodeJsRetrofitClient
import com.example.apphoctap.network.RetrofitInstance
import com.example.apphoctap.network.api.UserRequest
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val client: ChatClient
) : ViewModel() {

    private val repository = RetrofitInstance.authRepository
    private val nodeJsApiService = NodeJsRetrofitClient.apiService
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
                            val name = payload.optString("username", "")
                            val role = body.role

                            try {
                                val userIdRequest = UserRequest(
                                    userId = userId,
                                    name = name,
                                    image = "",
                                    role = role,
                                    extraData = mutableMapOf(
                                        "email" to email,
                                    )

                                )
                                val chatTokenResponse = nodeJsApiService.getChatToken(userIdRequest)
                                val chatToken = chatTokenResponse.token
                                Log.d("Chat Token ----------", chatToken)
                                // lấy token từ response
                                val user = ExposedUser(
                                    userID = userId,
                                    username = name,
                                    email = email,
                                    password = "",
                                    role = role,
                                    sdt = ""
                                )

                                sessionManager.saveUserSession(body.token, body.refreshToken, role, chatToken)
                                Log.d("Chat", "Chat token: $chatToken")

                                //connect user
                                val chatUser = User(
                                    id = userId,
                                    name = name,
                                    image = "",
                                    role = role,
                                    extraData = mutableMapOf(
                                        "email" to email,
                                    )
                                )

                                client.connectUser(chatUser, chatToken).enqueue { result ->
                                    if (result.isSuccess) {
                                        Log.d("Chat", "Connected to Stream Chat successfully")
                                    } else {
                                        Log.e("Chat", "Failed to connect: ${result.errorOrNull()}")
                                    }
                                }

                                loginState.value = com.example.apphoctap.model.LoginState.Success(
                                    token = body.token,
                                    refreshToken = body.refreshToken,
                                    user = user,
                                    chatToken = chatToken,
                                    message = "Đăng nhập thành công"
                                )




                            } catch (e: Exception) {
                                Log.e("Chat", "Error fetching chat token", e)
                                loginState.value = com.example.apphoctap.model.LoginState.Error("Không thể lấy token chat")
                            }

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
                loginState.value = com.example.apphoctap.model.LoginState.Error("LỖI KẾT NỐI MẠNG")
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
