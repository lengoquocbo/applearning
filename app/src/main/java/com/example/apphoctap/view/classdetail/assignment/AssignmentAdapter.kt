package com.example.apphoctap.view.classdetail.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.databinding.ItemAssignmentBinding
import com.example.apphoctap.model.AssignmentSubmission
import com.example.apphoctap.model.AttachmentResponse

class AssignmentAdapter(
    private var assignmentList: List<AssignmentSubmission>,
    private val onEdit: (AssignmentSubmission) -> Unit,
    private val onDelete: (AssignmentSubmission) -> Unit,
    private val onFileAttachClick: (AttachmentResponse) -> Unit,
    private val onSubmissionClick: (AssignmentSubmission) -> Unit,
    private val onClickListener: (AssignmentSubmission) -> Unit,
    private val role : String
) : RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(val binding: ItemAssignmentBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignmentViewHolder {
        val binding = ItemAssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssignmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = assignmentList[position]

        if (role == "TEACHER") {
            holder.binding.btnEditAssignment.visibility = ViewGroup.VISIBLE
            holder.binding.btnDeleteAssignment.visibility = ViewGroup.VISIBLE
            holder.binding.btnSubmitAssignment.visibility = ViewGroup.GONE
            holder.binding.tvScore.visibility = ViewGroup.GONE
            holder.binding.tvStatus.visibility = ViewGroup.GONE
            holder.binding.feedback.visibility = ViewGroup.GONE
            holder.binding.root.setOnClickListener{ onClickListener(assignment) }
        } else {
            holder.binding.btnEditAssignment.visibility = ViewGroup.GONE
            holder.binding.btnDeleteAssignment.visibility = ViewGroup.GONE
            holder.binding.btnSubmitAssignment.visibility = ViewGroup.VISIBLE
            holder.binding.tvScore.visibility = ViewGroup.VISIBLE
            holder.binding.tvStatus.visibility = ViewGroup.VISIBLE
            holder.binding.feedback.visibility = ViewGroup.VISIBLE

        }

        holder.binding.apply {
            tvDescription.text = assignment.description
            tvAssignmentTitle.text = assignment.title
            tvDueDate.text = "Due: ${assignment.dueDate}"
            tvPostDate.text = "Ngày tạo ${assignment.createdAt}"
            btnEditAssignment.setOnClickListener { onEdit(assignment) }
            btnDeleteAssignment.setOnClickListener { onDelete(assignment) }
            btnSubmitAssignment.setOnClickListener { onSubmissionClick(assignment) }
            tvScore.text = "Điểm : ${assignment.score}"
            if (assignment.status == "SUBMITTED") btnSubmitAssignment.visibility = ViewGroup.GONE
            tvStatus.text = "Trạng thái : ${assignment.status}"
            feedback.text = "Phản hồi : ${assignment.feedback}"
        }
        holder.binding.rvFileAttach.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FileAttachmentAdapter(assignment.attachments, object : OnFileClickListener {
                override fun onFileClick(file: AttachmentResponse) {
                    onFileAttachClick(file)
                }
            })
        }
    }

    fun updateList(newList: List<AssignmentSubmission>){
        assignmentList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = assignmentList.size
}

interface OnFileClickListener {
    fun onFileClick(file: AttachmentResponse)
}
