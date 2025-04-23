package com.example.apphoctap.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apphoctap.databinding.ActivityForgetpasswordBinding
import com.example.apphoctap.model.Exposedforget
import com.example.apphoctap.view.viewmodel.ForgetViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetpasswordBinding
    private val viewModel: ForgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnXacThucemail.setOnClickListener {
            val email = binding.edtemail.text.toString().trim()
            if (email.isNotEmpty()) {
                viewModel.sendEmail(Exposedforget(email))
            } else {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.forgetState.collectLatest { state ->
                when (state) {
                    is com.example.apphoctap.model.ForgetState.Success -> {
                        if (state.message == "success") {
                            val intent = Intent(this@ForgetActivity, CodeVerifyActivity::class.java)
                            intent.putExtra("email", binding.edtemail.text.toString())
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ForgetActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                        viewModel.resetState()
                    }

                    is com.example.apphoctap.model.ForgetState.Error -> {
                        Toast.makeText(this@ForgetActivity, state.error, Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                    }

                    else -> Unit // Idle hoặc Loading bạn có thể show progress nếu muốn
                }
            }
        }
    }
}
