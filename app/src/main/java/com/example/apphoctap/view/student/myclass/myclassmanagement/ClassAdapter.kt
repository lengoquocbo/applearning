package com.example.apphoctap.view.student.myclass.myclassmanagement

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ItemCourseBinding
import com.example.apphoctap.model.ClassUiModel


class ClassAdapter (
    private var classList : List<ClassUiModel>,
    private val studentId : String,
    val onDelete : (classId : String, studentId : String) -> Unit,
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder( val binding: ItemCourseBinding ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun getItemCount(): Int = classList.size

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classes = classList[position]
        holder.binding.apply {
            textViewCourseName.text = classes.className
            tvEnrollmentkey.text = classes.enrollmentKey
            tvDescription.text = classes.description
            textViewTeacherName.text = "GV: {classes.teacherName}"

            imageViewCourse.setOnClickListener{
                showPopupMenu(it, classes)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList : List<ClassUiModel>){
        classList = newList
        notifyDataSetChanged()
    }

    fun showPopupMenu(view : View, Class : ClassUiModel){
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.class_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.remove_class -> {
                    onDelete(Class.classId, studentId)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }




}