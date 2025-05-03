package com.example.apphoctap.view.teacher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apphoctap.databinding.ActivityTeacherBinding

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding

    // These variables should be declared inside onCreate
    private var userId: String? = null
    private var username: String? = null
    private var email: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent correctly
        userId = intent.getStringExtra("userID")
        username = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        role = intent.getStringExtra("role")

        // Chỉ add fragment nếu Activity vừa tạo (tránh bị đè khi back)
        if (savedInstanceState == null) {
            // Create fragment instance and pass data using Bundle
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("userID", userId)
                    putString("username", username)
                    putString("email", email)
                    putString("role", role)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(binding.frameContainer.id, homeFragment)
                .commit()
        }
    }
}