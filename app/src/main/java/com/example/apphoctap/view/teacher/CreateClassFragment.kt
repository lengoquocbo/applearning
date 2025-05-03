package com.example.apphoctap.view.teacher

import CreateClassViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentTeacherCreateclassBinding
import com.example.apphoctap.utils.SessionManager

class CreateClassFragment : Fragment() {

    private var _binding: FragmentTeacherCreateclassBinding? = null
    private val binding get() = _binding!!

    private val classViewModel: CreateClassViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherCreateclassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val teacherID = sessionManager.getTeacherID().orEmpty()
        val enrollmentKey = generateEnrollmentKey()

        binding.etClassKey.setText(enrollmentKey)

        binding.btnContinue.setOnClickListener {
            val className = binding.etClassName.text.toString().trim()
            val description = binding.etClassTopic.text.toString().trim()

            if (className.isBlank()) {
                showToast("Vui lòng nhập tên lớp")
                return@setOnClickListener
            }

            if (teacherID.isBlank()) {
                showToast("Không tìm thấy thông tin giáo viên")
                return@setOnClickListener
            }

            classViewModel.createClass(
                classID = "", // để server tạo
                teacherID = teacherID,
                className = className,
                description = description,
                enrollmentKey = enrollmentKey
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        classViewModel.createClassResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                showToast("Tạo lớp thành công")

                // Điều hướng sau khi tạo thành công (nếu dùng Navigation Component)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, HomeFragment())
                    .commit()

                // Hoặc: findNavController().navigate(R.id.action_createClass_to_classListFragment)
            }.onFailure {
                showToast("Tạo lớp thất bại: ${it.message}")
            }
        }
    }

    private fun generateEnrollmentKey(length: Int = 8): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
