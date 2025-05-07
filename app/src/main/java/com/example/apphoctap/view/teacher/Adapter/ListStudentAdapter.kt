package com.example.apphoctap.view.teacher.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.R
import com.example.apphoctap.model.StudentResponse

class ListStudentAdapter(
    private var studentList: List<StudentResponse>,
    private val onItemClick: (StudentResponse) -> Unit,
    private val onDelete: (StudentResponse) -> Unit
) : RecyclerView.Adapter<ListStudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvId: TextView = itemView.findViewById(R.id.tvStudentID)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMoreOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = student.studentname
        holder.tvId.text = "MSSV: ${student.studentID}"

        holder.itemView.setOnClickListener {
            onItemClick(student)
        }

        holder.btnMore.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.delete_popup_menu)
            popup.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_deletestudent) {
                    onDelete(student)
                    true
                } else false
            }
            popup.show()
        }
    }
    fun updateList(newList: List<StudentResponse>) {
        studentList = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = studentList.size
}
