package com.project.veganlife.data.model

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val nickname: String) : UiState()
    data class Error(val message: String?) : UiState()
}