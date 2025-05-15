package com.example.apphoctap.view.teacher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentTeacherHomeBinding
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.view.classdetail.ClassDetailFragment
import com.example.apphoctap.view.teacher.Adapter.ClassAdapter
import com.example.apphoctap.view.ui.teacher.AllClassFragment
import com.example.apphoctap.view.viewmodel.teacher.HomeTeacherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragmentTeacher : Fragment() {

    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding get() = _binding!!

    private val classViewModel: HomeTeacherViewModel by viewModels()

    private lateinit var classAdapter: ClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy token từ session
        val token = SessionManager(requireContext()).getAccessToken()
        val teacherID = token?.let { JwtUtils.getTeacherIDFromToken(it) }
        val username = token?.let { JwtUtils.getUsernameFormToken(it) }

        if (teacherID == null) {
            Toast.makeText(requireContext(), "Không tìm thấy teacherID", Toast.LENGTH_SHORT).show()
            return
        }

        // Thiết lập RecyclerView
        classAdapter = ClassAdapter(emptyList()) { classItem ->
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

        }

        binding.txtusename.text ="${binding.txtusename.text} $username"

        binding.classRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = classAdapter
        }

        // Quan sát LiveData từ ViewModel
        classViewModel.classList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { list ->
                classAdapter.updateData(list)
            }
            result.onFailure { e ->
                Toast.makeText(requireContext(), "Lỗi tải lớp: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Gọi API để lấy danh sách lớp
        classViewModel.getClassesByTeacherID(teacherID)
        binding.tvAllClass.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_teacher, AllClassFragment())
                .addToBackStack(null)
                .commit()
        }
        // Sự kiện tạo lớp
        binding.btnCreateClass.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_teacher, CreateClassFragment())
                .addToBackStack(null)
                .commit()
        }

        classViewModel.sumClassAndStudent(teacherID)

        // Quan sát kết quả
        classViewModel.sum.observe(viewLifecycleOwner) { result ->
            result?.let {
                // Hiển thị kết quả
                Log.d("ClassInfo", "Tổng lớp: ${it.sumClass}, Tổng học sinh: ${it.sumStudent}")
                binding.sumclass.text = "${it.sumClass}"
                binding.sumstudent.text = "${it.sumStudent}"
            }
        }

        // Quan sát lỗi
        classViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
