package com.example.apphoctap.view.teacher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.apphoctap.databinding.ActivityTeacherBinding
import com.example.apphoctap.R
import com.example.apphoctap.view.ui.teacher.AllClassFragment

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding

    private var userId: String? = null
    private var username: String? = null
    private var email: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent
        userId = intent.getStringExtra("userID")
        username = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        role = intent.getStringExtra("role")

        // Chỉ thêm Fragment nếu Activity mới tạo
        if (savedInstanceState == null) {
            loadFragment(HomeFragmentTeacher())
        }

        // Xử lý sự kiện khi chọn item trong BottomNavigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> HomeFragmentTeacher()
                R.id.navigation_class -> AllClassFragment()
//                R.id.navigation_chat -> ChatFragmentTeacher()
//                R.id.navigation_assignment -> AssignmentFragmentTeacher()
                R.id.navigation_profile -> ProfileFragment()
                else -> HomeFragmentTeacher()
            }

            // Truyền lại dữ liệu nếu cần
            selectedFragment.arguments = Bundle().apply {
                putString("userID", userId)
                putString("username", username)
                putString("email", email)
                putString("role", role)
            }

            loadFragment(selectedFragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameContainer.id, fragment)
            .commit()
    }
}
