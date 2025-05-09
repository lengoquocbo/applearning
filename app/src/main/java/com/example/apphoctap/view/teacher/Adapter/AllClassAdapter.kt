package com.example.apphoctap.view.teacher.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.R
import com.example.apphoctap.database.entities.ClassCacheEntitiy

class AllClassAdapter(
    private var classList: List<ClassCacheEntitiy>,
    private val onItemClick: (ClassCacheEntitiy, ) -> Unit,
    private val onEditClick: (ClassCacheEntitiy) -> Unit,
    private val onDeleteClick: (ClassCacheEntitiy) -> Unit
) : RecyclerView.Adapter<AllClassAdapter.AllClassViewHolder>() {

    inner class AllClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCourseName: TextView = itemView.findViewById(R.id.textViewCourseName)
        val tv_enrollmentkey: TextView = itemView.findViewById(R.id.tv_enrollmentkey)
        val tv_description: TextView = itemView.findViewById(R.id.tv_description)
        val textViewTeacherName: TextView = itemView.findViewById(R.id.textViewTeacherName)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return AllClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllClassViewHolder, position: Int) {
        val item = classList[position]
        holder.textViewCourseName.text = item.className
        holder.tv_enrollmentkey.text = item.enrollmentKey
        holder.tv_description.text = item.description
        holder.textViewTeacherName.text = "GV: ${item.teacherName}"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        holder.btnMore.setOnClickListener{ view ->
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.menu_class_item)
            popup.setOnMenuItemClickListener { menuItem->
                when (menuItem.itemId){
                    R.id.action_edit ->{
                        onEditClick(item)
                        true
                    }
                    R.id.action_delete ->{
                        onDeleteClick(item)
                        true
                    }
                    else -> false
                }

            }
            popup.show()

        }
    }


    override fun getItemCount(): Int = classList.size

    fun updateData(newList: List<ClassCacheEntitiy>) {
        classList = newList
        notifyDataSetChanged()
    }
}
