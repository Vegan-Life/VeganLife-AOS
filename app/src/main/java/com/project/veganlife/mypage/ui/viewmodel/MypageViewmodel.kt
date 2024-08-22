package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.usecase.ProfileGetUsecase
import com.project.veganlife.data.model.ProfileRequestDTO
import com.project.veganlife.mypage.domain.usecase.ProfileModifyUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class MypageViewmodel @Inject constructor(
    private val profileGetUsecase: ProfileGetUsecase,
    private val profileModifyUsecase: ProfileModifyUsecase,
) : ViewModel() {
    // 유저 정보 api Response
    private val _profileInfoResponse = MutableLiveData<ProfileResponse>()
    val profileInfoResponse: LiveData<ProfileResponse> get() = _profileInfoResponse

    // api 결과 코드 체크
    private val _responseCode = MutableLiveData<String?>()
    val responseCode: LiveData<String?> get() = _responseCode

    // 유저 정보 상태 체크
    private val _profileInfoState = MutableLiveData<Boolean>()
    val profileInfoState: LiveData<Boolean> get() = _profileInfoState

    // 프로필 사진 MultiPart
    private val _profilePhotoMultipart = MutableLiveData<MultipartBody.Part>()
    val profilePhotoMultipart: LiveData<MultipartBody.Part> get() = _profilePhotoMultipart

    // 프로필 수정 정보 RequestBody
    private val _profileModifyRequestBody = MutableLiveData<RequestBody>()
    val profileModifyRequestBody: LiveData<RequestBody> get() = _profileModifyRequestBody

    // 닉네임 허용 패턴
    private val NICKNAME_PATTERN = "[가-힣]{2,10}".toRegex()

    fun getUserInfo() {
        viewModelScope.launch {
            val response = profileGetUsecase.invoke()
            when (response) {
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

    fun getProfileModifyInfo() {
        val profileModifyDTO = profileModifyRequestBody.value ?: return
        val profilePhoto = profilePhotoMultipart.value ?: return
        viewModelScope.launch {
            val response = profileModifyUsecase.invoke(profileModifyDTO, profilePhoto)
            when (response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("get User Info Error", responseDescription)

                    when (response.errorCode) {
                        // 중복 닉네임
                        "409" -> {
                            getModifyResponse(response.errorCode)
                        }
                        // 토큰 사용시간 초과
                        "404" -> {
                            getModifyResponse(response.errorCode)
                        }
                    }
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "mypage modify Info Error",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    getModifyResponse("200")
                }
            }
        }
    }

    // 처음 수정화면 입장 시 받는 profile response
    fun setUserInfo(userInfo: ProfileResponse) {
        _profileInfoResponse.value = userInfo
    }

    // 수정 완료 버튼 클릭 시 받는 response
    fun getModifyResponse(responseCode: String?) {
        if (responseCode != null) _responseCode.value = responseCode
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

    fun isUserInfoStateCheck(userInfo: ProfileRequestDTO): Boolean {
        val isNicknameValid = isNicknameValid(userInfo.nickname)
        val isHeightValid = isHeightValid(userInfo.height)
        val isWeightValid = isWeightValid(userInfo.weight)

        return isNicknameValid && isHeightValid && isWeightValid
    }

    // 프로필 이미지
    fun putProfilePhotoMultipart(photo: MultipartBody.Part) {
        _profilePhotoMultipart.value = photo
    }

    // 프로필 정보
    fun putProfileRequestBody(profile: RequestBody) {
        _profileModifyRequestBody.value = profile
    }
}