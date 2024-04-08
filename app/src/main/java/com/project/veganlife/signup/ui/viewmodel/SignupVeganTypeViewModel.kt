package com.project.veganlife.signup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.R
import com.project.veganlife.signup.data.model.SignupVeganType
import com.project.veganlife.signup.domain.SignupVeganTypeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupVeganTypeViewModel @Inject constructor(
    private val signupVeganTypeUsecase: SignupVeganTypeUsecase,
) : ViewModel() {
    private val _veganTypeData = MutableLiveData<List<SignupVeganType>>()
    val veganTypeData: LiveData<List<SignupVeganType>> get() = _veganTypeData

    private val _veganTypeSelected = MutableLiveData<String>()
    val veganTypeSelected: LiveData<String> get() = _veganTypeSelected

    private val _btnBackgroundColor = MutableLiveData<Int>()
    val btnBackgroundColor: LiveData<Int> get() = _btnBackgroundColor

    private val _signupVeganTypeInfo = MutableLiveData<String>()
    val signupVeganTypeInfo: LiveData<String> get() = _signupVeganTypeInfo

    fun setVeganList() {
        viewModelScope.launch {
            val result = signupVeganTypeUsecase()
            _veganTypeData.value = result
        }
    }

    fun setSelectedVeganName(name: String) {
        _veganTypeSelected.value = name
        _signupVeganTypeInfo.value = name
        // 선택한 아이템이 있을 때는 배경색을 base3로, 없을 때는 gray3로 설정
        _btnBackgroundColor.value = if (!veganTypeSelected.value.isNullOrEmpty()) {
            R.color.base3
        } else {
            R.color.gray3
        }
    }

    fun clearSelectedVeganName() {
        _veganTypeSelected.value = ""
        _btnBackgroundColor.value = R.color.gray3
    }
}