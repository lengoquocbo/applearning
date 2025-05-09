package com.example.apphoctap.view.ui.teacher

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentManageListclassBinding
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.view.teacher.Adapter.AllClassAdapter
import com.example.apphoctap.view.classdetail.ClassDetailFragment
import com.example.apphoctap.view.teacher.CreateClassFragment
import com.example.apphoctap.view.viewmodel.teacher.AllClassViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllClassFragment : Fragment() {

    private var _binding: FragmentManageListclassBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllClassViewModel by viewModels()
    private lateinit var classAdapter: AllClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageListclassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = SessionManager(requireContext()).getAccessToken()
        val teacherID = token?.let { JwtUtils.getTeacherIDFromToken(it) }
        val username = token?.let { JwtUtils.getUsernameFormToken(it) }

        if (teacherID == null) {
            Toast.makeText(requireContext(), "Không tìm thấy teacherID", Toast.LENGTH_SHORT).show()
            return
        }

        setupClickListeners()

        // Thiết lập Adapter
        classAdapter = AllClassAdapter(
            classList = emptyList(),
            onItemClick = { classItem ->
                val classDetailFragment = ClassDetailFragment().apply {
                    arguments = bundleOf(
                        "classID" to classItem.classId,
                        "className" to classItem.className,
                        "teacherName" to classItem.teacherName,
                        "enrollmentKey" to classItem.enrollmentKey
                    )
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container_teacher, classDetailFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onEditClick = { classItem ->
                val editFragment = CreateClassFragment().apply {
                    arguments = bundleOf(
                        "classID" to classItem.classId,
                        "className" to classItem.className,
                        "enrollmentKey" to classItem.enrollmentKey
                    )
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container_teacher, editFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onDeleteClick = { classItem ->
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_delete_confirmation)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val tvMessage = dialog.findViewById<TextView>(R.id.tv_delete_message)
                tvMessage.text = "Bạn có chắc chắn muốn xóa lớp học \"${classItem.className}\"?"

                val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                val btnDelete = dialog.findViewById<Button>(R.id.btn_delete)
                btnDelete.setOnClickListener {
                    viewModel.deleteClass(classItem.classId)
                    Toast.makeText(requireContext(), "Đã xóa lớp: ${classItem.className}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    viewModel.getClassesByTeacherID(teacherID)
                }

                dialog.show()
            }

        )

        // Set Adapter cho RecyclerView
        binding.rvClassList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = classAdapter
        }

        binding.tvGreeting.text = "${binding.tvGreeting.text} $username"

        viewModel.getClassesByTeacherID(teacherID)
        observeClassList()
    }

    private fun setupClickListeners() {
        binding.fabAddClass.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_teacher, CreateClassFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnCreateFirstClass.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_teacher, CreateClassFragment())
                .addToBackStack(null)
                .commit()

            Toast.makeText(requireContext(), "Chuyển đến tạo lớp học đầu tiên", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeClassList() {
        viewModel.classList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { list ->
                classAdapter.updateData(list)
                binding.rvClassList.isVisible = list.isNotEmpty()
                binding.layoutEmptyState.isVisible = list.isEmpty()
            }

            result.onFailure { e ->
                Toast.makeText(requireContext(), "Lỗi khi tải lớp học: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.rvClassList.isVisible = false
                binding.layoutEmptyState.isVisible = true
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}