package com.example.apphoctap.view.student.myclass

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.R
import com.example.apphoctap.databinding.CourseFragmentBinding
import com.example.apphoctap.utils.JoinClassState
import com.example.apphoctap.utils.UiState
import com.example.apphoctap.view.classdetail.ClassDetailFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassFragment : Fragment() {
    private val viewModel: ClassViewModel by viewModels<ClassViewModel>()
    private lateinit var classAdapter : ClassAdapter
    private var _binding : CourseFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CourseFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Kiemtra", "onViewCreated được gọi")


        //Set up RecyclerView
        classAdapter = ClassAdapter(
            classList = emptyList(),
            onClick = {item->
                viewModel.onClassClicked(item.classId)
                val myClassDetailFragment = ClassDetailFragment().apply {
                    arguments = bundleOf(
                        "classID" to item.classId,
                        "className" to item.className,
                        "teacherName" to item.teacherName,
                        "enrollmentKey" to item.enrollmentKey
                    )
            }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container_student, myClassDetailFragment)
                    .addToBackStack(null)
                    .commit()

            },
            onDelete = {
                deleteClass (it)
            }
        )
        binding.recyclerViewMyCourses.adapter = classAdapter
        binding.recyclerViewMyCourses.layoutManager = LinearLayoutManager(requireContext())

        //Gọi hàm quan sát dữ liệu
        observeViewModel()

        Log.d("DEBUG", "binding.buttonJoinCourse: ${binding.buttonJoinCourse}")


        //Gọi hàm xử lý nút
        setUpUI()

        //Gọi hàm quan sát hành động tham gia lớp học
        observeViewModelJoinClass()

        observedeleteViewModel()
    }

    private fun setUpUI() {
        binding.buttonJoinCourse.setOnClickListener{
            val enrollmentKey = binding.editTextCourseCode.text.toString()
            Log.d("DEBUG", "Đã bấm tham gia: $enrollmentKey")

            if (enrollmentKey.isBlank()) {
                binding.textInputLayoutCourseCode.error = "Vui lòng nhập mã tham gia lớp học"
                return@setOnClickListener
            } else {
                binding.textInputLayoutCourseCode.error = null
            }

            // Gọi ViewModel để xử lý tham gia lớp học
            viewModel.joinClass(enrollmentKey)
            viewModel.loadClasses()
        }

        // Thiết lập sự kiện xóa lỗi khi người dùng nhập text
        binding.editTextCourseCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textInputLayoutCourseCode.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModelJoinClass() {
        // Quan sát trạng thái tham gia lớp học
        viewModel.joinClassState.observe(viewLifecycleOwner) { state ->
            if (state is JoinClassState.Success) viewModel.loadClasses()
            handleJoinClassState(state)
        }
    }

    private fun handleJoinClassState(state: JoinClassState) {
        when (state) {
            is JoinClassState.Idle -> {
                // Trạng thái ban đầu, không làm gì
                binding.progressBar.isVisible = false
                binding.buttonJoinCourse.isEnabled = true
            }
            is JoinClassState.Loading -> {
                // Đang tải, hiển thị progress bar và vô hiệu hóa nút
                binding.progressBar.isVisible = true
                binding.buttonJoinCourse.isEnabled = false
            }
            is JoinClassState.Success -> {
                // Tham gia thành công
                binding.progressBar.isVisible = false
                binding.buttonJoinCourse.isEnabled = true

                // Hiển thị thông báo thành công
                Snackbar.make(
                    binding.root,
                    "Tham gia lớp học ${state.classData.className} thành công",
                    Snackbar.LENGTH_SHORT
                ).show()

                // Cập nhật danh sách lớp học
                classAdapter.updateList(listOf(state.classData))
            }
            is JoinClassState.Error -> {
                // Tham gia thất bại, hiển thị lỗi
                binding.progressBar.isVisible = false
                binding.buttonJoinCourse.isEnabled = true

                // Hiển thị thông báo lỗi
                Snackbar.make(
                    binding.root,
                    state.message,
                    Snackbar.LENGTH_LONG
                ).setAction("Thử lại") {
                    // Nếu người dùng bấm "Thử lại", bắt đầu lại quá trình
                    val enrollmentKey = binding.editTextCourseCode.text.toString().trim()
                    if (enrollmentKey.isNotBlank()) {
                        viewModel.joinClass(enrollmentKey)
                    }
                }.show()
            }
        }
    }

    private fun observedeleteViewModel() {
        // Quan sát trạng thái rời lớp học
        viewModel.leaveClassState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LeaveClassState.Idle -> {
                    binding.progressBar.isVisible = false
                }
                is LeaveClassState.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is LeaveClassState.Success -> {
                    binding.progressBar.isVisible = false
                    Snackbar.make(
                        binding.root,
                        "Rời lớp học thành công",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // Cập nhật danh sách lớp học nếu cần

                }
                is LeaveClassState.Error -> {
                    binding.progressBar.isVisible = false
                    Snackbar.make(
                        binding.root,
                        state.message,
                        Snackbar.LENGTH_LONG
                    ).setAction("Thử lại") {
                        // Gọi lại deleteClass với cùng tham số
                        deleteClass(classId = state.classId)
                    }.show()
                }
                else -> {

                }
            }
        }
    }

    fun deleteClass(classId: String) {

        // Hiển thị AlertDialog để xác nhận
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Rời khỏi lớp")
        dialog.setMessage("Bạn có chắc chắn muốn rời lớp này?")
        dialog.setPositiveButton("Có") { _, _ ->
            viewModel.leaveClass(classId)
        }
        dialog.setNegativeButton("Không", null)
        dialog.show()
    }

    private fun observeViewModel() {
        viewModel.classes.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is UiState.Loading -> {
                    binding.shimmerContainer.visibility = View.VISIBLE
                    binding.recyclerViewMyCourses.visibility = View.GONE
                    binding.textViewError.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.shimmerContainer.visibility = View.GONE
                    binding.textViewError.visibility = View.GONE

                    resource.data.let { classes ->
                        if (classes.isEmpty()) {
                            binding.layoutEmptyCourses.visibility = View.VISIBLE
                            binding.recyclerViewMyCourses.visibility = View.GONE
                        } else {
                            binding.layoutEmptyCourses.visibility = View.GONE
                            classAdapter.updateList(classes)
                            binding.recyclerViewMyCourses.visibility = View.VISIBLE
                        }

                        // Hiển thị trạng thái offline nếu có
                        val anyFromCache = classes.any { it.isFromCache }
                        if (anyFromCache) {
                            binding.textViewOfflineIndicator.visibility = View.VISIBLE
                        } else {
                            binding.textViewOfflineIndicator.visibility = View.GONE
                        }
                    }
                }
                is UiState.Error -> {
                    binding.shimmerContainer.visibility = View.GONE
                    binding.textViewError.visibility = View.VISIBLE
                    binding.textViewError.text = resource.message ?: "Unknown error"
                }
            }
        }


        viewModel.operationStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is UiState.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), resource.data, Toast.LENGTH_SHORT).show()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}