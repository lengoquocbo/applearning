package com.example.apphoctap.view.classdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apphoctap.view.classdetail.assignment.AssignmentsFragment
import com.example.apphoctap.view.classdetail.assignment.detailassignment.DetailAssignmentFragment
import com.example.apphoctap.view.classdetail.list.ListStudentFragment
import com.example.apphoctap.view.classdetail.material.MaterialsFragment

class ViewPagerAdapter(
    fragment: Fragment,
    private val classID: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = AssignmentsFragment()
                val bundle = Bundle().apply {
                    putString("classID", classID)
                }
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = ListStudentFragment()
                val bundle = Bundle().apply {
                    putString("classID", classID)
                }
                fragment.arguments = bundle
                fragment
            }
            2 -> {
                val fragment = MaterialsFragment()
                val bundle = Bundle().apply {
                    putString("classID", classID)
                }
                fragment.arguments = bundle
                fragment
            }
            3 -> DetailAssignmentFragment()
            else -> Fragment()
        }
    }
}

