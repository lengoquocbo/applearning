package com.example.apphoctap.view.teacher.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.R
import com.example.apphoctap.database.entities.ClassCacheEntitiy

class ClassAdapter(
    private var classList: List<ClassCacheEntitiy>,
    private val onItemClick: (ClassCacheEntitiy) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivClassImage: ImageView = itemView.findViewById(R.id.ivClassImage)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvClassName: TextView = itemView.findViewById(R.id.tvClassName)
        val tvTeacherName: TextView = itemView.findViewById(R.id.tv_tengiangvien)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(classList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val item = classList[position]
        holder.tvClassName.text = item.className
        holder.tvDescription.text = item.description
        holder.tvTeacherName.text = item.teacherName

        // Nếu bạn có hình ảnh, load bằng Glide hoặc Picasso
        // Glide.with(holder.itemView.context).load(imageUrl).into(holder.ivClassImage)
//        holder.ivClassImage.setImageResource(R.drawable.sample_class_image) // placeholder
    }

    override fun getItemCount(): Int = classList.size

    fun updateData(newList: List<ClassCacheEntitiy>) {
        classList = newList
        notifyDataSetChanged()
    }
}
