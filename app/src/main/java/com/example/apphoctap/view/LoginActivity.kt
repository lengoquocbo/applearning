package com.example.apphoctap.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apphoctap.databinding.ActivityLoginBinding
import com.example.apphoctap.model.ExposedUser
import com.example.apphoctap.model.ExposedUserLogin
import com.example.apphoctap.model.LoginState
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.view.student.StudentActivity
import com.example.apphoctap.view.teacher.TeacherActivity
import com.example.apphoctap.view.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateBasedOnRole(sessionManager.getUserRole() ?: "", sessionManager.getUserDetails())
            finish()
            return
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginCredentials = ExposedUserLogin(email, password)
            viewModel.login(loginCredentials)
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        observeLoginState()
    }

    private fun observeLoginState() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.LoginState.collect { state ->
                when (state) {
                    is LoginState.Loading -> withContext(Dispatchers.Main) {
                        binding.btnLogin.isEnabled = false
                    }

                    is LoginState.Success -> withContext(Dispatchers.Main) {

                        binding.btnLogin.isEnabled = true

                        sessionManager.saveUserSession(
                            state.token,
                            state.refreshToken,
                            state.user.role
                        )

                        navigateBasedOnRole(state.user.role, state.user)
                        finish()
                    }

                    is LoginState.Error -> withContext(Dispatchers.Main) {
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this@LoginActivity, state.error, Toast.LENGTH_SHORT).show()
                    }

                    LoginState.Idle -> { /* Do nothing */ }
                }
            }
        }
    }

    private fun navigateBasedOnRole(role: String, user: ExposedUser) {
        val intent = when (role) {
            "STUDENT" -> Intent(this, StudentActivity::class.java)
            "TEACHER" -> Intent(this, TeacherActivity::class.java)
            else -> {
                Toast.makeText(this, "Không xác định được vai trò người dùng", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Lấy teacherID và studentID từ SessionManager
        val teacherID = sessionManager.getTeacherID()
        val studentID = sessionManager.getStudentID()

        intent.putExtra("userID", user.userID)
        intent.putExtra("username", user.username)
        intent.putExtra("email", user.email)
        intent.putExtra("role", user.role)
        intent.putExtra("teacherID", teacherID)
        intent.putExtra("studentID", studentID)

        startActivity(intent)
    }
}