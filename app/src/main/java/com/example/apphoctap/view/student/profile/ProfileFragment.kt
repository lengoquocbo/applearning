package com.example.apphoctap.view.teacher


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

import androidx.fragment.app.Fragment
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ProfileFragmentBinding
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.view.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class ProfileFragmentStudent : Fragment() {
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = SessionManager(requireContext()).getAccessToken()
        val username = token?.let { JwtUtils.getUsernameFormToken(it) }
        val email = token?.let { JwtUtils.getEmailFromToken(it) }
        val sdt = token?.let { JwtUtils.getSdtFromToken(it) }

        binding.tvName.text = username
        binding.tvEmail.text = email
        binding.tvPhone.text = sdt

        // MỞ DIALOG CHỈNH SỬA THÔNG TIN
        binding.btnEditProfile.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_edit_profile)

            val editName = dialog.findViewById<TextInputEditText>(R.id.etName)
            val editEmail = dialog.findViewById<TextInputEditText>(R.id.etEmail)
            val editPhone = dialog.findViewById<TextInputEditText>(R.id.etPhone)
            val editPass = dialog.findViewById<TextInputEditText>(R.id.etPassword)

            val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSave)
            val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)

            // Hiển thị thông tin hiện tại
            editName.setText(username)
            editEmail.setText(email)
            editPhone.setText(sdt)

            btnSave.setOnClickListener {
                // TODO: Gửi thông tin mới lên server hoặc xử lý local
                dialog.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        // MỞ DIALOG XÁC NHẬN ĐĂNG XUẤT
        binding.btnLogOut.setOnClickListener {
            // 1. Clear local token
            SessionManager(requireContext()).clearSession()

            // 2. Show Snackbar
            Snackbar.make(binding.root, "Đăng xuất thành công", Snackbar.LENGTH_SHORT).show()

            // 3. Navigate to LoginActivity sau một chút delay để thấy Snackbar
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }, 1000) // 1 giây
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
