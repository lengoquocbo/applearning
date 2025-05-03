package com.example.apphoctap.utils

import com.example.apphoctap.model.ClassUiModel

sealed class JoinClassState {
    object Idle : JoinClassState()
    object Loading : JoinClassState()
    data class Success(val classData: ClassUiModel) : JoinClassState()
    data class Error(val message: String) : JoinClassState()
}