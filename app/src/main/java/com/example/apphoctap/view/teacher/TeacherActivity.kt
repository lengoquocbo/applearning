package com.example.apphoctap.view.teacher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.apphoctap.databinding.ActivityTeacherBinding
import com.example.apphoctap.R
import com.example.apphoctap.view.ProfileNavigator
import com.example.apphoctap.view.chat.ChannelListFragment
import com.example.apphoctap.view.document.DocumentFragment
import com.example.apphoctap.view.ui.teacher.AllClassFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherActivity : AppCompatActivity(), ProfileNavigator {
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

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragmentTeacher())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_class -> {
                    loadFragment(AllClassFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_chat -> {
                    loadFragment(ChannelListFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_assignment -> {
                    loadFragment(DocumentFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }

        }// Truyền lại dữ liệu nếu cần

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_teacher, fragment)
            .commit()
    }


    override fun openProfile() {
        binding.bottomNavigation.selectedItemId = R.id.navigation_profile
    }
}
