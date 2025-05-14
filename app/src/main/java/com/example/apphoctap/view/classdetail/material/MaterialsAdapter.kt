package com.example.apphoctap.view.classdetail.material

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.databinding.ItemFileBinding
import com.example.apphoctap.model.ClassMaterial

class MaterialsAdapter(
    private var materialList: List<ClassMaterial>,
    private val onClickListener: (ClassMaterial) -> Unit
) : RecyclerView.Adapter<MaterialsAdapter.MaterialViewHolder>(){


    class MaterialViewHolder(val binding : ItemFileBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding)
    }

    override fun getItemCount(): Int = materialList.size

    fun updateList(newList: List<ClassMaterial>) {
        materialList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = materialList[position]
        holder.binding.apply {
            tvDocumentName.text = material.fileName
            tvDocumentDate.text = material.dateCreated
            root.setOnClickListener { onClickListener(material) }
        }
    }
}