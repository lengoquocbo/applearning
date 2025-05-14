package com.example.apphoctap.view.classdetail.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.databinding.ItemFileAttachBinding
import com.example.apphoctap.model.AttachmentResponse

class FileAttachmentAdapter(
    private val files: List<AttachmentResponse>,
    private val onClickListener : OnFileClickListener
) : RecyclerView.Adapter<FileAttachmentAdapter.FileViewHolder>() {

    class FileViewHolder(val binding : ItemFileAttachBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ItemFileAttachBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.binding.apply {
            tvFileName.text = file.fileName
        }

        holder.itemView.setOnClickListener({
            onClickListener.onFileClick(file)
        })

    }


}