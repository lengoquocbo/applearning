package com.example.apphoctap.view.teacher.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apphoctap.view.teacher.AssignmentsFragment
import com.example.apphoctap.view.teacher.ListStudentFragment
import com.example.apphoctap.view.teacher.MaterialsFragment

class ViewPagerAdapter(
    fragment: Fragment,
    private val classID: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AssignmentsFragment()
            1 -> {
                val fragment = ListStudentFragment()
                val bundle = Bundle().apply {
                    putString("classID", classID)
                }
                fragment.arguments = bundle
                fragment
            }
            2 -> MaterialsFragment()
            else -> Fragment()
        }
    }
}

