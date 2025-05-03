package com.example.apphoctap.view.student.document

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apphoctap.view.student.document.filemanagement.FileManageFragment
import com.example.apphoctap.view.student.document.flashcardmanagement.FlashcardManageFragment
import com.example.apphoctap.view.student.document.DocumentFragment

class DocumentViewPageAdapter(documentFragment : DocumentFragment) : FragmentStateAdapter(documentFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FileManageFragment()
            else -> FlashcardManageFragment()
        }
    }

}