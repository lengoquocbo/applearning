package com.example.apphoctap.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apphoctap.databinding.ActivityForgetFillincodeBinding
import com.example.apphoctap.model.CodeState
import com.example.apphoctap.model.ExposedCode
import com.example.apphoctap.viewmodel.CodeVerifyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CodeVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetFillincodeBinding
    private lateinit var progressBar: ProgressBar
    private val viewModel: CodeVerifyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetFillincodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thêm ProgressBar vào layout bằng code nếu bạn chưa có trong XML
        progressBar = ProgressBar(this).apply {
            visibility = View.GONE
            isIndeterminate = true
        }
        addContentView(progressBar, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        val email = intent.getStringExtra("email") ?: ""

        binding.btnXacNhan.setOnClickListener {
            val code = binding.edtcode.text.toString().trim()
            if (code.isNotEmpty()) {
                viewModel.verifyCode(ExposedCode(email, code))
            } else {
                Toast.makeText(this, "Vui lòng nhập mã xác thực", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe ViewModel
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is CodeState.Idle -> {
                        progressBar.visibility = View.GONE
                    }
                    is CodeState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is CodeState.Success -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@CodeVerifyActivity, state.message, Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@CodeVerifyActivity, ResetPasswordActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                        viewModel.resetState()
                    }
                    is CodeState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@CodeVerifyActivity, state.error, Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                    }
                }
            }
        }
    }
}
