package com.example.apphoctap.view.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ActivityStudentBinding
import com.example.apphoctap.view.ProfileNavigator
import com.example.apphoctap.view.chat.ChannelListFragment
import com.example.apphoctap.view.student.myclass.ClassFragment
import com.example.apphoctap.view.document.DocumentFragment
import com.example.apphoctap.view.teacher.ProfileFragmentStudent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentActivity : AppCompatActivity(), ProfileNavigator {
    private lateinit var binding: ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    loadFragment(com.example.apphoctap.view.student.home.HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_class -> {
                    loadFragment(ClassFragment())
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
                    loadFragment(ProfileFragmentStudent())
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }

        //Set default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home // Chọn mục Home làm mặc định
        }
    }

    // Hàm load fragment
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_student, fragment)
            .commit()
    }

    override fun openProfile() {
        binding.bottomNavigation.selectedItemId = R.id.navigation_profile
    }
}