package com.example.apphoctap.view.classdetail.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apphoctap.model.AssignmentSubmission

class SharedViewModel : ViewModel() {

    private val _selectedAssignment = MutableLiveData<AssignmentSubmission>()
    val selectedAssignment: LiveData<AssignmentSubmission> = _selectedAssignment

    fun selectAssignment(assignment: AssignmentSubmission) {
        _selectedAssignment.value = assignment
    }
}