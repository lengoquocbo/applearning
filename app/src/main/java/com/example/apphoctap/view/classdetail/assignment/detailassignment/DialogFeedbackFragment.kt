package com.example.apphoctap.view.classdetail.assignment.detailassignment

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.apphoctap.R

class DialogFeedbackFragment(
    private val onSendClick: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_feedback, null)

        val etFeedback = view.findViewById<EditText>(R.id.etFeedback)

        builder.setView(view)
            .setTitle("Gửi phản hồi")
            .setPositiveButton("Gửi") { _, _ ->
                val feedback = etFeedback.text.toString().trim()
                if (feedback.isNotEmpty()) {
                    onSendClick(feedback)
                } else {
                    Toast.makeText(context, "Vui lòng nhập phản hồi!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }
}
