package com.example.apphoctap.view.classdetail.list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentManageListstudentBinding
import com.example.apphoctap.model.AddStudentRequest
import com.example.apphoctap.model.DeleteStudentRequest
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListStudentFragment : Fragment() {

    private var _binding: FragmentManageListstudentBinding? = null
    private val binding get() = _binding!!
    private var role: String? = null

    private lateinit var studentAdapter: ListStudentAdapter
    private val viewModel: ListStudentViewModel by viewModels()

    lateinit var classID : String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageListstudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy token và role từ SessionManager
        val token = SessionManager(requireContext()).getAccessToken()
        role = token?.let { JwtUtils.getRoleFromToken(it) }

        classID = arguments?.getString("classID").toString()  // Gán classID

        if (classID == null) {
            Toast.makeText(requireContext(), "Lỗi: classID không tồn tại", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("class ID", "class Id $classID")

        // Hiển thị hoặc ẩn nút thêm học sinh dựa vào role
        if (role == "TEACHER") {
            binding.btnAddstudent.visibility = View.VISIBLE
        } else {
            binding.btnAddstudent.visibility = View.GONE
        }

        setupRecyclerView()
        setupClickListeners()

        // Gọi ViewModel để lấy dữ liệu
        viewModel.getStudentByClassID(classID!!)

        // Quan sát dữ liệu trả về
        viewModel.studentList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { students ->
                studentAdapter.updateList(students)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        studentAdapter = ListStudentAdapter(
            studentList = emptyList(),
            onItemClick = { studentItem ->
                Toast.makeText(requireContext(), "Đã chọn: ${studentItem.studentname}", Toast.LENGTH_SHORT).show()
            },
            onDelete = { studentItem ->
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_delete_confirmation)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val tvMessage = dialog.findViewById<TextView>(R.id.tv_delete_message)
                tvMessage.text = "Bạn có chắc chắn muốn xóa Học Sinh \"${studentItem.studentname}\" không?"

                val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                val btnDelete = dialog.findViewById<Button>(R.id.btn_delete)
                btnDelete.setOnClickListener {
                    val request = DeleteStudentRequest(studentItem.studentID, classID)
                    Log.d("Delete Student", "DeleteStudentRequest: $request")
                    viewModel.deleteStudent(request)
                    Toast.makeText(requireContext(), "Đã xóa học sinh: ${studentItem.studentname}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    classID?.let { viewModel.getStudentByClassID(it) }
                }

                dialog.show()
            },
            role = role ?: "" // truyền role vào adapter
        )

        binding.rvListStudent.apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnAddstudent.setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun showAddStudentDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_student)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val emailEditText = dialog.findViewById<EditText>(R.id.emailEditText)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val addButton = dialog.findViewById<Button>(R.id.addButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        addButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val request = AddStudentRequest(email = email, classID = classID)
            Log.d("Add Student", "AddStudentRequest: $request")
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show()
            } else if (classID == null) {
                Toast.makeText(requireContext(), "Lỗi: Không tìm thấy thông tin lớp", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                viewModel.addStudentByEmail(request)
                Toast.makeText(requireContext(), "Đã thêm học sinh: $email", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                // Cập nhật lại danh sách học sinh sau khi thêm
                viewModel.getStudentByClassID(classID!!)
            }
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}