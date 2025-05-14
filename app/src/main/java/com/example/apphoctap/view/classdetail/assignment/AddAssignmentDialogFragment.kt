package com.example.apphoctap.view.classdetail.assignment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apphoctap.R
import com.example.apphoctap.databinding.DialogCreateAssignmentBinding
import com.example.apphoctap.databinding.ItemAttachmentBinding
import com.example.apphoctap.model.AttachmentItem
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddAssignmentDialogFragment : DialogFragment() {

    private var _binding: DialogCreateAssignmentBinding? = null
    private val binding get() = _binding!!
    private val attachmentsList = mutableListOf<AttachmentItem>()
    private lateinit var attachmentAdapter: AttachmentAdapter
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    private var listener: OnCreateAssignmentListener? = null

    // Interface callback cho việc tạo assignment
    interface OnCreateAssignmentListener {
        fun onAssignmentCreated(
            title: String,
            description: String,
            dueDate: String,
            attachments: List<AttachmentItem>
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Kiểm tra xem parent fragment hoặc activity có implement listener không
        if (parentFragment is OnCreateAssignmentListener) {
            listener = parentFragment as OnCreateAssignmentListener
        } else if (context is OnCreateAssignmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context hoặc $parentFragment phải implement OnCreateAssignmentListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCreateAssignmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cài đặt style cho dialog
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        setupDatePicker()
        setupAttachmentsList()
        setupButtons()
    }

    private fun setupDatePicker() {
        binding.etDueDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    showTimePickerDialog()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun showTimePickerDialog() {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                binding.etDueDate.setText(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setupAttachmentsList() {
        attachmentAdapter = AttachmentAdapter(attachmentsList) { attachment ->
            attachmentsList.remove(attachment)
            attachmentAdapter.notifyDataSetChanged()
        }
        binding.rvAttachments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = attachmentAdapter
        }
    }

    // File picker sử dụng registerForActivityResult
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val mimeType = getMimeType(it)
            val allowedTypes = listOf(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
            )
            if (mimeType !in allowedTypes) {
                Toast.makeText(requireContext(), "Chỉ cho phép file PDF, Word, hoặc TXT", Toast.LENGTH_SHORT).show()
                return@let
            }
            val fileName = getFileNameFromUri(it)
            attachmentsList.add(AttachmentItem(fileName, it, mimeType))
            attachmentAdapter.notifyDataSetChanged()
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            try {
                requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            result = cursor.getString(nameIndex)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result ?: uri.lastPathSegment ?: "Tệp không xác định"
    }


    private fun getMimeType(uri: Uri): String? {
        return requireContext().contentResolver.getType(uri) ?: run {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase(Locale.getDefault()))
        }
    }

    private fun setupButtons() {
        binding.btnAddAttachment.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnCreate.setOnClickListener {
            if (validateInputs()) {
                val title = binding.etTitle.text.toString()
                val description = binding.etDescription.text.toString()
                val dueDate = binding.etDueDate.text.toString()

                listener?.onAssignmentCreated(title, description, dueDate, attachmentsList)
                dismiss()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        if (binding.etTitle.text.toString().isEmpty()) {
            binding.etTitle.error = "Vui lòng nhập tiêu đề"
            isValid = false
        }
        if (binding.etDueDate.text.toString().isEmpty()) {
            binding.etDueDate.error = "Vui lòng chọn hạn nộp"
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    //phần để hiển thị danh sách filedđã được add
    // Adapter để hiển thị attachments
    inner class AttachmentAdapter(
        private val attachments: List<AttachmentItem>,
        private val onRemoveListener: (AttachmentItem) -> Unit
    ) : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = attachments[position]
            holder.bind(item)
        }

        override fun getItemCount() = attachments.size

        inner class ViewHolder(private val binding: ItemAttachmentBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: AttachmentItem) {
                binding.tvFileName.text = item.fileName

                // Đặt icon dựa vào loại file
                val iconRes = when {
                    item.mimeType?.contains("pdf") == true -> R.drawable.ic_pdf
                    item.mimeType?.contains("word") == true ||
                            item.mimeType?.contains("document") == true -> R.drawable.ic_docfile
                    item.mimeType?.contains("text") == true -> R.drawable.ic_txtfile
                    else -> R.drawable.ic_file
                }
                Glide.with(requireContext()).load(iconRes).into(binding.ivFileIcon)

                binding.btnRemoveFile.setOnClickListener {
                    onRemoveListener(item)
                }
            }
        }
    }

    companion object {
        fun newInstance(): AddAssignmentDialogFragment {
            return AddAssignmentDialogFragment()
        }
    }
}