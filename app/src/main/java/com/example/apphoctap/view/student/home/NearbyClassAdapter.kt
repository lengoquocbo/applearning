package com.example.apphoctap.view.student.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.databinding.ItemClassBinding
import com.example.apphoctap.model.ClassUiModel

class NearbyClassAdapter(
    private var classList : List<ClassUiModel>,
    val onClick : (ClassUiModel) -> Unit
) : RecyclerView.Adapter<NearbyClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(val binding : ItemClassBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun getItemCount(): Int = classList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList : List<ClassUiModel>){
        classList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classes = classList[position]
        holder.binding.apply {
            tvDescription.text = classes.description
            tvClassName.text = classes.className
            tvTengiangvien.text = classes.teacherName

            root.setOnClickListener {
                onClick(classes)
            }
        }

    }


}