package com.example.apphoctap.view.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentTeacherHomeBinding
import com.example.apphoctap.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding get() = _binding!!

    private var teacherID: String? = null
    private var userId: String? = null
    private var username: String? = null
    private var email: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.getAccessToken()

        // Lấy teacherID từ token
        teacherID = token?.let { com.example.apphoctap.utils.JwtUtils.getTeacherIDFromToken(it) }

        // Lấy thông tin từ arguments nếu có
        arguments?.let {
            userId = it.getString("userID")
            username = it.getString("username")
            email = it.getString("email")
            role = it.getString("role")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hiển thị teacherID
        binding.txtusename.text = "${binding.txtusename.text} $teacherID"

        // Bắt sự kiện tạo lớp
        binding.btnCreateClass.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, CreateClassFragment())
                .commit()  // Không cần addToBackStack nếu không cần quay lại fragment trước đó
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
