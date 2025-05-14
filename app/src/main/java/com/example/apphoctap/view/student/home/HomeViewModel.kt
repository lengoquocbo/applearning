package com.example.apphoctap.view.student.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ClassUiModel
import com.example.apphoctap.repository.ClassRepository
import com.example.apphoctap.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val classRepository: ClassRepository
) : ViewModel() {
    private val _classes = MutableLiveData<UiState<List<ClassUiModel>>>(UiState.Loading)
    val classes: LiveData<UiState<List<ClassUiModel>>> = _classes

    init{
        loadClasses()
    }

    fun loadClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _classes.value = UiState.Loading
                delay(500)
            }
            try {
                val result = classRepository.getNearbyAccessClasses()
                withContext(Dispatchers.Main){
                    _classes.value = UiState.Success(result)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _classes.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun onClassClicked(classId: String) {
        viewModelScope.launch {
            classRepository.updateClassAccessTime(classId)
        }
    }


}