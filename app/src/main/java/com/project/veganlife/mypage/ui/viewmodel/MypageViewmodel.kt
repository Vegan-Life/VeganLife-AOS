package com.project.veganlife.mypage.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.usecase.ProfileGetUsecase
import com.project.veganlife.mypage.data.model.MypageModifyRequestDTO
import com.project.veganlife.mypage.domain.usecase.MypageUsecase
import com.project.veganlife.utils.PhotoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewmodel @Inject constructor(
    private val profileGetUsecase: ProfileGetUsecase,
    private val mypageUsecase: MypageUsecase
) : ViewModel() {
    // 유저 정보 api Response
    private val _profileInfoResponse = MutableLiveData<ProfileResponse>()
    val profileInfoResponse: LiveData<ProfileResponse> get() = _profileInfoResponse

    // api 결과 코드 체크
    private val _responseCode = MutableLiveData<String?>()
    val responseCode: LiveData<String?> get() = _responseCode

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
                        "getProfile Exception",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    setUserInfo(response.data)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun modifyProfile(
        context: Context,
        nickname: String,
        vegetarianType: String,
        gender: String,
        birthYear: Int,
        height: Int,
        weight: Int,
        existingImageUrl: String?,
        profileUris: Uri?
    ) {
        val requestDTO = PhotoUtils.createRequestBody(
            MypageModifyRequestDTO(
                nickname = nickname,
                vegetarianType = vegetarianType,
                gender = gender,
                birthYear = birthYear,
                height = height,
                weight = weight,
                existingImageUrl = existingImageUrl
            )
        )

        val imagePath = profileUris?.let { PhotoUtils.optimizeBitmap(context, it) }
        Log.d("### url", "imagePath: $imagePath")

        val imageMultipart = PhotoUtils.createImageMultipart(imagePath)
        Log.d("### url", "imageMultipart: $imageMultipart, size: ${imageMultipart?.body?.contentLength()}")

        viewModelScope.launch {
            val response = mypageUsecase.modifyProfile(requestDTO, imageMultipart)
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

    fun isUserInfoStateCheck(nickname:String, height: Int, weight: Int): Boolean {
        val isNicknameValid = isNicknameValid(nickname)
        val isHeightValid = isHeightValid(height)
        val isWeightValid = isWeightValid(weight)

        return isNicknameValid && isHeightValid && isWeightValid
    }
}