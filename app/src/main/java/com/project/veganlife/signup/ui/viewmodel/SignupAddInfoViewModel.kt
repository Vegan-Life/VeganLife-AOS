package com.project.veganlife.signup.ui.viewmodel

import android.content.SharedPreferences
import android.provider.ContactsContract.Profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileRequestDTO
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.data.model.UiState
import com.project.veganlife.signup.data.model.SignupAddInfo
import com.project.veganlife.signup.data.model.SignupRequest
import com.project.veganlife.signup.domain.usecase.SignupUsecase
import com.project.veganlife.utils.PhotoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SignupAddInfoViewModel @Inject constructor(
    private val signupUsecase: SignupUsecase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _signupInfo = MutableStateFlow(SignupAddInfo())
    val signupInfo: StateFlow<SignupAddInfo> = _signupInfo.asStateFlow()

    // 모든 입력 유효성 상태 (true면 버튼 활성화)
    val isAllValid: StateFlow<Boolean> = _signupInfo
        .map { it.validate().isEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // 서버 응답 상태
    private val _signupState = MutableStateFlow<UiState>(UiState.Idle)
    val signupState: StateFlow<UiState> = _signupState.asStateFlow()

    fun updateInfo(update: SignupAddInfo.() -> SignupAddInfo) {
        _signupInfo.update { it.update() }
    }

    fun setVeganType(veganType: String) {
        updateInfo { copy(vegetarianType = veganType) }
    }

    fun submitSignup() {
        _signupState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val request = createSignupRequest()
                val result = signupUsecase.signupAddInfo(request) // suspend 함수 직접 호출
                handleSignupResult(result)
            } catch (e: Exception) {
                _signupState.value = UiState.Error(e.message)
            }
        }
    }

    private fun createSignupRequest(): RequestBody {
        val requestDto = SignupRequest(
            nickname = _signupInfo.value.nickname,
            gender = _signupInfo.value.gender,
            vegetarianType = _signupInfo.value.vegetarianType,
            birthYear = _signupInfo.value.birthYear ?: 0,
            height = _signupInfo.value.height ?: 0,
            weight = _signupInfo.value.weight ?: 0
        )
        return PhotoUtils.createRequestBody(requestDto)
    }

    private fun handleSignupResult(result: ApiResult<ProfileResponse>) {
        _signupState.value = when (result) {
            is ApiResult.Success -> {
                sharedPreferences.edit()
                    .putString("Nickname", result.data.nickname)
                    .apply()
                UiState.Success(result.data.nickname)
            }
            is ApiResult.Error -> UiState.Error(result.description)
            is ApiResult.Exception -> UiState.Error(result.e.message)
        }
    }
}