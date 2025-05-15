package com.example.apphoctap.view.classdetail.assignment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentAssignmentBinding
import com.example.apphoctap.model.AssignmentSubmission
import com.example.apphoctap.model.AttachmentItem
import com.example.apphoctap.model.AttachmentResponse
import com.example.apphoctap.utils.AssignmentCreationState
import com.example.apphoctap.utils.AssignmentState
import com.example.apphoctap.utils.ResultAssignment
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.utils.UploadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class AssignmentsFragment : Fragment(),
    AddAssignmentDialogFragment.OnCreateAssignmentListener,
    AddSubmissionDialogFragment.OnCreateSubmissionListener,
    EditAssignmentDialogFragment.onEditAssignmentListener
{

    private var _binding: FragmentAssignmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var assignmentAdapter: AssignmentAdapter
    private val viewModel : AssignmentViewModel by viewModels()
    lateinit var classID : String
    private  val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2


    @Inject lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classID = arguments?.getString("classID").toString()  // Gán classID

        Log.d("class ID", "class Id $classID")

        viewModel.getAssignments(classID)
        setupUI()
        observeCreateViewModel()
        observeViewModel()
    }


    private fun setupUI() {


        if (sessionManager.getUserRole() == "TEACHER") {
            binding.fabAddAssignment.visibility = View.VISIBLE
        } else {
            binding.fabAddAssignment.visibility = View.GONE
        }
        binding.fabAddAssignment.setOnClickListener {
            openAddAssignmentDialog()
        }

        //lấy dữ liệu từ argument
        assignmentAdapter = AssignmentAdapter(
            assignmentList = emptyList(),
            onEdit = { assignment ->
                editAssignment(assignment)
            },
            onDelete = { assignment ->
                deleteAssignment(assignment)
            },
            onFileAttachClick = { file ->
                fileAttachClick(file)
            },
            onSubmissionClick = { assignment ->
                submitAssignment(assignment)

            },
            onClickListener = { assignment ->
                sharedViewModel.selectAssignment(assignment)
                Log.d("AssignmentsFragment", "selected assignment: $assignment")
                Log.d("AssignmentsFragment", "------------${sharedViewModel.selectedAssignment.value}")
                viewPager = requireParentFragment().requireView().findViewById(R.id.viewPager)
                viewPager.currentItem = 3
            },
            role = sessionManager.getUserRole()
        )

        viewModel.downloadUrl.observe(viewLifecycleOwner) { (url, name) ->
            downloadFile(requireContext(), url, name)
        }

        binding.recyclerViewAssignments.adapter = assignmentAdapter
        binding.recyclerViewAssignments.layoutManager = LinearLayoutManager(requireContext())

    }


    private fun fileAttachClick(file: AttachmentResponse){
        viewModel.fetchDownloadUrl(file.id, file.fileName)
    }

    private fun downloadFile(context: Context, url: String, fileName: String) {
        val request = android.app.DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Đang tải xuống...")
            .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
        dm.enqueue(request)
        Toast.makeText(requireContext(), "File $fileName downloaded", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAssignment(assignment: AssignmentSubmission) {
        showDeleteConfirmationDialog(assignment)
    }

    private fun showDeleteConfirmationDialog(assignment : AssignmentSubmission) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_delete, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.setCancelable(false)

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnDelete.setOnClickListener {
            // Thực hiện hành động xóa ở đây
            viewModel.deleteAssignment(assignment.id)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun editAssignment(assignment: AssignmentSubmission) {
        val dialog = EditAssignmentDialogFragment.newInstance(assignment)
        dialog.show(childFragmentManager, "EditAssignmentDiaLog")
    }


    private fun submitAssignment(assignment: AssignmentSubmission) {
        val dialog = AddSubmissionDialogFragment.newInstance(assignment)
        dialog.show(childFragmentManager, "AddAssignmentDialog")
    }

    private fun observeCreateViewModel() {
        viewModel.creationState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is AssignmentCreationState.Loading -> showLoading(true)
                is AssignmentCreationState.Success -> {
                    showLoading(false)
                    showSuccessMessage("Đã tạo bài tập thành công")
                    // Làm mới danh sách bài tập hoặc điều hướng nếu cần
                    loadAssignments()
                }
                is AssignmentCreationState.Error -> {
                    showLoading(false)
                    showErrorMessage(state.message)
                }
                else -> { }
            }
        }
    }

    private fun openAddAssignmentDialog() {
        val dialogFragment = AddAssignmentDialogFragment.newInstance()
        dialogFragment.show(childFragmentManager, "AddAssignmentDialog")
    }

    override fun onAssignmentCreated(
        title: String,
        description: String,
        dueDate: String,
        attachments: List<AttachmentItem>
    ) {
        val uris = attachments.map { it.fileUri }

        viewModel.uploadFilesAndCreateAssignment(uris, classID, title, description, dueDate)
    }

    override fun onSubmissionCreated(assignment: AssignmentSubmission, attachments: List<AttachmentItem>) {
        val uris = attachments.map { it.fileUri }
        viewModel.uploadFilesAndSubmitAssignment(uris, assignment, classID)
    }

    override fun onEditAssignmnetListener(
        title: String,
        description: String,
        dueDate: String,
        assignmentSubmission: AssignmentSubmission
    ) {
        viewModel.updateAssignment(assignmentSubmission.id, title, description, dueDate)
    }

    private fun observeViewModel(){
        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UploadState.Loading -> {
                    // Hiển thị loading indicator
                    showLoading(true)
                }
                is UploadState.Success -> {
                    // Ẩn loading indicator
                    showLoading(false)

                }
                is UploadState.Error -> {
                    // Ẩn loading indicator
                    showLoading(false)
                    showErrorMessage("Upload không thành công")
                }
                else -> {}
            }
        }

        viewModel.updateAssignment.observe(viewLifecycleOwner){ state ->
            when (state) {
                is ResultAssignment.Loading -> {
                    // Hiển thị loading state
                    showLoading(true)
                }
                is ResultAssignment.Success -> {
                    // Ẩn loading và hiển thị thông báo thành công
                    showLoading(false)
                    showSuccessMessage("Cập nhật bài tập thành công")
                    // Refresh dữ liệu hoặc cập nhật UI
                    loadAssignments()
                }
                is ResultAssignment.Error -> {
                    // Ẩn loading và hiển thị thông báo lỗi
                    showLoading(false)
                    showErrorMessage(state.message)
                }
            }
        }

        viewModel.submission.observe(viewLifecycleOwner){ state ->
            when (state){
                is AssignmentState.Creating -> {
                    showLoading(true)
                }
                is AssignmentState.Created -> {
                    showLoading(false)
                    showSuccessMessage("Đã nộp bài tập thành công!")
                    loadAssignments()
                }
                is AssignmentState.Error -> {
                    showLoading(false)
                }
                else -> {}
            }

        }

        viewModel.assignmentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AssignmentState.Creating -> {
                }
                is AssignmentState.Created -> {
                    // Assignment đã được tạo thành công
                    showSuccessMessage("Đã tạo bài tập thành công!")
                    loadAssignments()
                }
                is AssignmentState.Error -> {
                    showErrorMessage(state.message)
                }
                else -> {}
            }
        }

        viewModel.assignments.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultAssignment.Loading -> {
                    showLoading(true)
                    binding.recyclerViewAssignments.visibility = View.GONE
                    binding.layoutEmptyAssignments.visibility = View.GONE
                }

                is ResultAssignment.Success -> {
                    showLoading(false)
                    val data = state.data

                    if (data.isEmpty()) {
                        binding.recyclerViewAssignments.visibility = View.GONE
                        binding.layoutEmptyAssignments.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewAssignments.visibility = View.VISIBLE
                        binding.layoutEmptyAssignments.visibility = View.GONE
                        assignmentAdapter.updateList(data)  // đảm bảo bạn đã khai báo adapter trước
                    }
                }

                is ResultAssignment.Error -> {
                    showLoading(false)
                    binding.recyclerViewAssignments.visibility = View.GONE
                    binding.layoutEmptyAssignments.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deleteAssignmentResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultAssignment.Loading -> {
                    // Hiển thị loading state
                    showLoading(true)
                }
                is ResultAssignment.Success -> {
                    // Ẩn loading và hiển thị thông báo thành công
                    showLoading(false)
                    showSuccessMessage("Đã xóa bài tập thành công")
                    // Refresh dữ liệu hoặc cập nhật UI
                    loadAssignments()
                }
                is ResultAssignment.Error -> {
                    // Ẩn loading và hiển thị thông báo lỗi
                    showLoading(false)
                    showErrorMessage(result.message)
                }
            }
        }
    }

    private fun loadAssignments() {
        // Gọi lại API để tải lại danh sách bài tập
        viewModel.getAssignments(classID)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSuccessMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showSuccessMessageSnackbar(message: String) {
        // Hiển thị thông báo thành công bằng Snackbar hoặc Toast
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showErrorMessageSnackbar(message: String) {
        // Hiển thị thông báo lỗi bằng Snackbar hoặc Toast
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }


}


