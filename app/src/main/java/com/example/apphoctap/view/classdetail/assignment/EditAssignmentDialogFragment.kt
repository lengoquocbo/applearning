package com.example.apphoctap.view.classdetail.assignment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.apphoctap.databinding.DialogEditAssignmentBinding
import com.example.apphoctap.model.AssignmentSubmission
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditAssignmentDialogFragment(private val assignmentSubmission: AssignmentSubmission) : DialogFragment() {

    private var _binding : DialogEditAssignmentBinding? = null
    private val binding get() = _binding!!
    private val calendar: Calendar = Calendar.getInstance()
    private var listener : onEditAssignmentListener? = null
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())


    interface onEditAssignmentListener {
        fun onEditAssignmnetListener(
            title: String,
            description: String,
            dueDate: String,
            assignmentSubmission: AssignmentSubmission
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Kiểm tra xem parent fragment hoặc activity có implement listener không
        if (parentFragment is onEditAssignmentListener) {
            listener = parentFragment as onEditAssignmentListener
        } else if (context is onEditAssignmentListener) {
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
        _binding = DialogEditAssignmentBinding.inflate(inflater, container, false)
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
        setUpUI()
    }

    private fun setUpUI() {
        binding.etTitle.setText(assignmentSubmission.title)
        binding.etDueDate.setText(assignmentSubmission.dueDate)
        binding.etDescription.setText(assignmentSubmission.description)
        binding.btnCancel.setOnClickListener{dismiss()}
        binding.btnCreate.setOnClickListener {
            if (validateInputs()) {
                val title = binding.etTitle.text.toString()
                val description = binding.etDescription.text.toString()
                val dueDate = binding.etDueDate.text.toString()

                listener?.onEditAssignmnetListener(title, description, dueDate, assignmentSubmission)
                dismiss()
            }
        }
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

    companion object {
        fun newInstance(assignment: AssignmentSubmission): EditAssignmentDialogFragment {
            return EditAssignmentDialogFragment(assignment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}