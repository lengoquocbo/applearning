package com.example.apphoctap.view.classdetail.assignment.detailassignment

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.databinding.DetailAssignmentFramentBinding
import com.example.apphoctap.model.AssignmentSubmission
import com.example.apphoctap.model.AttachmentResponse
import com.example.apphoctap.model.SubmissionsResponse
import com.example.apphoctap.utils.ResultAssignment
import com.example.apphoctap.view.classdetail.assignment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAssignmentFragment : Fragment() {

    private var _binding : DetailAssignmentFramentBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val viewModel : DetailAssignmentViewModel by viewModels()
    private lateinit var detailAssignmentAdapter : DetailAssignmentAdapter
    private lateinit var assignmentsave : AssignmentSubmission

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailAssignmentFramentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailAssignmentFragment", "onViewCreated")

        sharedViewModel.selectedAssignment.observeForever { assignment ->
            if (assignment != null) {
                assignmentsave = assignment
                viewModel.getDetailAssignment(assignment.id)
            } else Log.e("Error", "assignment is null")
        }
        setUpUI()
        observeViewModel()
    }

    private fun setUpUI() {
        detailAssignmentAdapter = DetailAssignmentAdapter(
            emptyList(),
            onFileAttachClick = { file ->
                fileAttachClick(file)
            },
            onFeedBackClick = { submission ->
                SendFeedBack(submission)
            }
        )

        binding.recyclerViewSubmissions.adapter = detailAssignmentAdapter
        binding.recyclerViewSubmissions.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun downloadFile(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Đang tải xuống...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(requireContext(), "File $fileName downloaded", Toast.LENGTH_SHORT).show()
    }

    fun fileAttachClick(file: AttachmentResponse){
        viewModel.fetchDownloadUrl(file.id, file.fileName)
    }

    fun SendFeedBack(submission : SubmissionsResponse){
        val dialog = DialogFeedbackFragment{ feedback ->
            viewModel.sendFeedback(submission.submitId, feedback)
        }

        dialog.show(parentFragmentManager, "DialogFeedbackFragment")
    }

    private fun loadData() {
        viewModel.getDetailAssignment(assignmentId = assignmentsave.id)
    }

    private fun observeViewModel() {
        viewModel.assignmentDetail.observe(viewLifecycleOwner) { state ->
            when(state) {
                is ResultAssignment.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultAssignment.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = state.data

                    if (data.isEmpty()) {
                        binding.layoutEmptyAssignments.visibility = View.VISIBLE
                        binding.recyclerViewSubmissions.visibility = View.GONE
                    } else {
                        binding.layoutEmptyAssignments.visibility = View.GONE
                        binding.recyclerViewSubmissions.visibility = View.VISIBLE
                        detailAssignmentAdapter.updateList(data)
                    }

                }
                is ResultAssignment.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmptyAssignments.visibility = View.VISIBLE
                    binding.recyclerViewSubmissions.visibility = View.GONE

                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }


            viewModel.downloadUrl.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { (url, name) ->
                    downloadFile(requireContext(), url, name)
                }
            }
        }

        viewModel.feedbackSent.observe(viewLifecycleOwner) { isSent ->
            if (isSent) {
                // Xử lý khi gửi feedback thành công
                Toast.makeText(requireContext(), "Gửi feedback thành công", Toast.LENGTH_SHORT).show()
                loadData()
                // Có thể thêm code để đóng dialog hoặc chuyển màn hình
            } else {
                // Xử lý khi gửi feedback thất bại
                Toast.makeText(requireContext(), "Gửi feedback thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show()
            }
        }

    }


}