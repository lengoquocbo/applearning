package com.example.apphoctap.view.classdetail.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.ClassMaterial
import com.example.apphoctap.repository.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MaterialsViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel(){

    private val _material = MutableLiveData<List<ClassMaterial>>()
    val material: MutableLiveData<List<ClassMaterial>> = _material

    fun getMaterials(classId: String) {
        viewModelScope.launch {
            val result = fileRepository.getMaterials(classId)
            _material.value = result
        }
    }

}