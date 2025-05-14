package com.example.apphoctap.view.document.filemanagement

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.databinding.FragmentFilesBinding
import com.example.apphoctap.model.AttachmentItem
import com.example.apphoctap.model.FileItem
import com.example.apphoctap.utils.FileResult
import com.example.apphoctap.utils.UploadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileManageFragment : Fragment(), AddFileDiaLog.OnCreateFileListener {
    private var _binding : FragmentFilesBinding? = null
    private val binding get() = _binding!!
    private lateinit var fileAdapter: FileAdapter
    private val viewModel : FileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFiles()
        setupUI()
        observeViewModel()
    }

    fun setupUI(){

        fileAdapter = FileAdapter(
            emptyList(),
            onItemClickListener = { file ->
                fileAttachClick(file)
            }
        )

        binding.rvDocuments.adapter = fileAdapter
        binding.rvDocuments.layoutManager = LinearLayoutManager(requireContext())

        binding.fabAddDocument.setOnClickListener({
            val dialog = AddFileDiaLog.newInstance()
            dialog.show(childFragmentManager, "AddFileDiaLog")
        })

        viewModel.downloadUrl.observe(viewLifecycleOwner) { (url, name) ->
            downloadFile(requireContext(), url, name)
        }
    }

    fun fileAttachClick(file: FileItem){
        viewModel.fetchDownloadUrl(file.fileId, file.fileName)
    }

    private fun downloadFile(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Đang tải xuống...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(requireContext(), "File $fileName downloaded", Toast.LENGTH_SHORT).show()
    }


    override fun onSubmissionCreated(attachments: List<AttachmentItem>) {
        val uris = attachments.map { it.fileUri }
        viewModel.uploadFiles(uris)
        loadFiles()
    }

    fun observeViewModel(){

        viewModel.filesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is FileResult.Loading -> {
                }
                is FileResult.Success -> {
                    val data = result.data

                    if (data.isEmpty()){
                        binding.tvEmptyDocuments.visibility = View.VISIBLE
                        binding.rvDocuments.visibility = View.GONE
                    }else{
                        binding.tvEmptyDocuments.visibility = View.GONE
                        binding.rvDocuments.visibility = View.VISIBLE
                        fileAdapter.updateList(data)
                    }
                }
                is FileResult.Error -> {
                    Snackbar.make(binding.root, "Load dữ liệu không thành công", Snackbar.LENGTH_SHORT).show()
                }
                is FileResult.Empty -> {
                    binding.tvEmptyDocuments.visibility = View.VISIBLE
                    binding.rvDocuments.visibility = View.GONE
                }
            }
        }

        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UploadState.Loading -> {
                    // Hiển thị loading indicator
                }
                is UploadState.Success -> {
                    // Ẩn loading indicator
                    showSuccessMessageSnackbar("Upload thành công")
                    loadFiles()
                }
                is UploadState.Error -> {
                    // Ẩn loading indicator
                    showErrorMessageSnackbar("Upload không thành công")
                }
                else -> {}
            }
        }


    }

    private fun showSuccessMessageSnackbar(message: String) {
        // Hiển thị thông báo thành công bằng Snackbar hoặc Toast
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showErrorMessageSnackbar(message: String) {
        // Hiển thị thông báo lỗi bằng Snackbar hoặc Toast
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }




    fun loadFiles() {
        viewModel.getFiles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}