package com.project.veganlife.mypage.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.usecase.ProfileGetUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MypageViewmodel @Inject constructor(
    private val profileGetUsecase: ProfileGetUsecase,
): ViewModel(){
    private val _resultUserInfo = MutableLiveData<ApiResult<ProfileResponse>>()
    val resultUserInfo: LiveData<ApiResult<ProfileResponse>> get() = _resultUserInfo

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _photo = MutableLiveData<String>()
    val photo: LiveData<String> get() = _photo

    fun getUserInfo() {
        viewModelScope.launch {
            _resultUserInfo.value = profileGetUsecase.invoke()
        }
    }

    fun setUserInfo(userInfo: ProfileResponse) {
        _nickname.value = userInfo.nickname
        _email.value = userInfo.email
        _photo.value = userInfo.imageUrl
    }
}