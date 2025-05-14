package com.example.apphoctap.view.classdetail.assignment.detailassignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.databinding.ItemSubmissionTeacherBinding
import com.example.apphoctap.model.AttachmentResponse
import com.example.apphoctap.model.SubmissionsResponse
import com.example.apphoctap.view.classdetail.assignment.FileAttachmentAdapter
import com.example.apphoctap.view.classdetail.assignment.OnFileClickListener

class DetailAssignmentAdapter (
    private var submission : List<SubmissionsResponse>,
    private val onFileAttachClick: (AttachmentResponse) -> Unit,
    private val onFeedBackClick: (SubmissionsResponse) -> Unit
) : RecyclerView.Adapter<DetailAssignmentAdapter.DetailAssignmentViewHolder>(){

    class DetailAssignmentViewHolder(val binding : ItemSubmissionTeacherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAssignmentViewHolder {
        val binding = ItemSubmissionTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailAssignmentViewHolder(binding)
    }

    override fun getItemCount(): Int = submission.size

    override fun onBindViewHolder(holder: DetailAssignmentViewHolder, position: Int) {
        val submission = submission[position]

        holder.binding.apply {
            tvStudentName.text = submission.studentName
            tvSubmittedAt.text = submission.uploadedAt
            tvFeedback.text = "Phản hồi: ${submission.feedBack}"

            btnFeedback.setOnClickListener({ onFeedBackClick(submission) })
        }

        holder.binding.rvFiles.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FileAttachmentAdapter(submission.fileSub, object : OnFileClickListener {
                override fun onFileClick(file: AttachmentResponse) {
                    onFileAttachClick(file)
                }
            })
        }
    }

    fun updateList(newList: List<SubmissionsResponse>){
        submission = newList
        notifyDataSetChanged()
    }
}