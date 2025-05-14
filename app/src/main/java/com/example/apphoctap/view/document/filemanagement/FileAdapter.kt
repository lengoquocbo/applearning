package com.example.apphoctap.view.document.filemanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.databinding.ItemFileBinding
import com.example.apphoctap.model.FileItem

class FileAdapter(
    private var fileList : List<FileItem>,
    private val onItemClickListener: (FileItem) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileHolder>() {

    class FileHolder(val binding : ItemFileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileHolder(binding)
    }

    fun updateList(newList: List<FileItem>){
        fileList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fileList.size

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val file = fileList[position]
        holder.binding.tvDocumentName.text = file.fileName
        holder.binding.tvDocumentDate.text = file.createAt

        holder.binding.root.setOnClickListener { onItemClickListener(file) }

    }
}