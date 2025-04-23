package com.example.apphoctap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Model người dùng
@Parcelize
data class ExposedUser(
    val userID: String = "",
    val username: String = "",
    val email: String = "",
    val sdt: String = "",
    val password: String = "",
    val role: String = ""
) : Parcelable

// Model dùng để gửi thông tin đăng nhập
@Parcelize
data class ExposedUserLogin(
    val email: String,
    val password: String
) : Parcelable
// nhận dữ liệu từ server
data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val role: String
)
data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

// Trạng thái khi login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(
        val token: String,
        val user: ExposedUser,
        val refreshToken: String,
        val message: String
    ) : LoginState()

    data class Error(val error: String) : LoginState()
}
data class Exposedforget(
    val email: String
)

data class forgetRespone(
    val message: String
)

sealed class ForgetState {
    object Idle : ForgetState()
    object Loading : ForgetState()
    data class Success(val message: String) : ForgetState()
    data class Error(val error: String) : ForgetState()
}

data class ExposedCode(
    val email: String,
    val code: String
)

data class CodeResponse(
    val message: String
)

sealed class CodeState {
    object Idle : CodeState()
    object Loading : CodeState()
    data class Success(val message: String) : CodeState()
    data class Error(val error: String) : CodeState()

}



data class ExposeNewPass(
    val email: String,
    val newPassword: String,
)
data class ResponseNewPass(
    val message: String
)
sealed class NewPassState {
    object Idle : NewPassState()
    object Loading : NewPassState()
    data class Success(val message: String) : NewPassState()
    data class Error(val error: String) : NewPassState()
}

