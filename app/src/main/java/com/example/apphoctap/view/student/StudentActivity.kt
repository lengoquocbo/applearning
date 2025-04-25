package com.example.apphoctap.view.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ActivityStudentBinding
import com.example.apphoctap.view.student.ui.chat.ChatFragment
import com.example.apphoctap.view.student.ui.course.CourseFragment
import com.example.apphoctap.view.student.ui.document.DocumentFragment
import com.example.apphoctap.view.student.ui.home.HomeFragment
import com.example.apphoctap.view.student.ui.profile.ProfileFragment

class StudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set actionBar
        setSupportActionBar(binding.toolbar)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_courses -> {
                    loadFragment(CourseFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_message -> {
                    loadFragment(ChatFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_document -> {
                    loadFragment(DocumentFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
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
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}