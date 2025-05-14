package com.example.apphoctap.view.document.filemanagement

import android.app.Dialog
import android.content.Context
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apphoctap.R
import com.example.apphoctap.databinding.DialogAddFileBinding
import com.example.apphoctap.databinding.ItemAttachmentBinding
import com.example.apphoctap.model.AttachmentItem

import java.util.Locale

class AddFileDiaLog : DialogFragment(){
    private var _binding : DialogAddFileBinding? = null
    private val binding get() = _binding!!
    private val attachmentsList = mutableListOf<AttachmentItem>()
    private lateinit var attachmentAdapter : AttachFileAdapter

    private var listener : OnCreateFileListener? = null

    interface OnCreateFileListener {
        fun onSubmissionCreated(
            attachments: List<AttachmentItem>
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Kiểm tra xem parent fragment hoặc activity có implement listener không
        if (parentFragment is OnCreateFileListener) {
            listener = parentFragment as OnCreateFileListener
        } else if (context is OnCreateFileListener) {
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
    ): View? {
        _binding = DialogAddFileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cài đặt style cho dialog
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        setUpAttachmentList()
        setUpButton()
    }

    private fun setUpButton() {
        binding.btnAddAttachment.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnAddSubmission.setOnClickListener {
            listener?.onSubmissionCreated(attachmentsList)
            dismiss()
        }
    }

    private fun setUpAttachmentList() {
        attachmentAdapter = AttachFileAdapter(attachmentsList) { attachment ->
            attachmentsList.remove(attachment)
            attachmentAdapter.notifyDataSetChanged()
        }
        binding.rvAttachments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = attachmentAdapter
        }
    }

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


    inner class AttachFileAdapter(
        private val attachments: List<AttachmentItem>,
        private val onRemoveListener: (AttachmentItem) -> Unit
    ) : RecyclerView.Adapter<AddFileDiaLog.AttachFileAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = attachments[position]
            holder.bind(item)
        }

        override fun getItemCount() = attachments.size

        inner class ViewHolder(private val binding: ItemAttachmentBinding) :
            RecyclerView.ViewHolder(binding.root) {

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
        fun newInstance(): AddFileDiaLog {
            return AddFileDiaLog()
        }
    }
}