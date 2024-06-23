package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.usecase.ProfileGetUsecase
import com.project.veganlife.mypage.data.model.ProfileModifyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MypageViewmodel @Inject constructor(
    private val profileGetUsecase: ProfileGetUsecase,
): ViewModel(){
    // 유저 정보 api Response
    private val _profileInfoResponse = MutableLiveData<ProfileResponse>()
    val profileInfoResponse: LiveData<ProfileResponse> get() = _profileInfoResponse

    // 유저 정보 수정 api Request
    private val _profileInfoRequest = MutableLiveData<ProfileResponse>()
    val profileInfoRequest: LiveData<ProfileResponse> get() = _profileInfoRequest

    // 유저 정보 상태 체크
    private val _profileInfoState = MutableLiveData<Boolean>()
    val profileInfoState: LiveData<Boolean> get() = _profileInfoState

    // 비건 타입 밸류
    private val _vegetarianType = MutableLiveData<String>()
    val vegetarianType: LiveData<String> get() = _vegetarianType


    private val NICKNAME_PATTERN = "[가-힣]{2,10}".toRegex()

    fun getUserInfo() {
        viewModelScope.launch {
            val response = profileGetUsecase.invoke()
            when(response) {
                is ApiResult.Error -> {
                    val response = response.description
                    Log.d("get User Info Error", response)
                }
                is ApiResult.Exception -> {
                    Log.d(
                        "recommended Exception",
                        response.e.message ?: "No message available"
                    )
                }
                is ApiResult.Success -> {
                    setUserInfo(response.data)
                }
            }
        }
    }

    fun setUserInfo(userInfo: ProfileResponse) {
        _profileInfoResponse.value = userInfo
    }

    // 닉네임 조건식
    private fun isNicknameValid(nickname: String): Boolean {
        return nickname.matches(NICKNAME_PATTERN)
    }

    // 키 조건식
    private fun isHeightValid(value: Int?): Boolean {
        return value != null && value > 0 && value <= 200
    }

    // 몸무게 조건식
    private fun isWeightValid(value: Int?): Boolean {
        return value != null && value > 0 && value <= 150
    }

    fun isUserInfoStateCheck(userInfo: ProfileModifyRequest): Boolean {
        val isNicknameValid =  isNicknameValid(userInfo.nickname)
        val isHeightValid = isHeightValid(userInfo.height)
        val isWeightValid = isWeightValid(userInfo.weight)

        return isNicknameValid && isHeightValid && isWeightValid
    }

    fun setVeganType(type: String) {
        _vegetarianType.value = type
    }
}