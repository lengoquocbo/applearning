package com.example.apphoctap.view.classdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apphoctap.databinding.FragmentCourseDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassDetailFragment: Fragment() {
    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val classID = arguments?.getString("classID")
        val teacherName = arguments?.getString("teacherName")
        val enrollmentKey = arguments?.getString("enrollmentKey")
        val className = arguments?.getString("className")

        binding.tvClassName.text = className
        binding.tvEnrollmentkey.text = enrollmentKey

        val adapter = ViewPagerAdapter(this, classID ?: "")
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nhiệm vụ"
                1 -> "Sinh Viên"
                2 -> "Tài liệu"
                else -> ""
            }
        }.attach()

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}
