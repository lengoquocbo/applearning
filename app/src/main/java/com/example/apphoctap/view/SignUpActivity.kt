package com.example.apphoctap.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apphoctap.R
import com.example.apphoctap.model.ExposedUser
import com.example.apphoctap.model.RegisterState
import com.example.apphoctap.view.viewmodel.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var rdbTeacher: RadioButton
    private lateinit var rdbStudent: RadioButton
    private lateinit var btnSignUp: Button
    private lateinit var tvLogin: TextView

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etsdt)
        etPassword = findViewById(R.id.etPassword)
        rdbTeacher = findViewById(R.id.rdbTeacher)
        rdbStudent = findViewById(R.id.rdbStudent)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLogin = findViewById(R.id.tvLogin)

        btnSignUp.setOnClickListener {
            val username = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val sdt = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = if (rdbTeacher.isChecked) "TEACHER" else "STUDENT"

            if (username.isEmpty() || email.isEmpty() || sdt.isEmpty() || password.isEmpty()|| role.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = ExposedUser("", username, email, sdt, password, role)
            viewModel.register(user)
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    is RegisterState.Loading -> {
                        // Nếu muốn: hiển thị progress bar
                    }
                    is RegisterState.Success -> {

                        Toast.makeText(this@SignUpActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    }
                    is RegisterState.Error -> {
                        Toast.makeText(this@SignUpActivity, "Email đã tồn tại", Toast.LENGTH_SHORT).show()
                    }
                    RegisterState.Idle -> { /* Không làm gì */ }
                }
            }
        }
    }
}
