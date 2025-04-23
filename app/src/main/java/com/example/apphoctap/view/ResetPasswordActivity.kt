package com.example.apphoctap.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apphoctap.databinding.ActivityResetpasswordBinding
import com.example.apphoctap.model.ExposeNewPass
import com.example.apphoctap.model.NewPassState
import com.example.apphoctap.view.viewmodel.ResetPasswordViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetpasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email") ?: ""

        binding.btnResetPassword.setOnClickListener {
            val newPassword = binding.edtNewPassword.text.toString().trim()

            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.resetPassword(ExposeNewPass(email, newPassword))
        }

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is NewPassState.Loading -> {
                        // Có thể hiện progress bar nếu muốn
                    }
                    is NewPassState.Success -> {
                        Toast.makeText(this@ResetPasswordActivity, "Đặt Mật Khẩu Thành Công", Toast.LENGTH_LONG).show()
                        Toast.makeText(this@ResetPasswordActivity, state.message, Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
                        finish()
                    }
                    is NewPassState.Error -> {
                        Toast.makeText(this@ResetPasswordActivity, state.error, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}
