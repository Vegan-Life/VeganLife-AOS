package com.project.veganlife.signup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.R
import com.project.veganlife.signup.data.model.SignupVeganType
import com.project.veganlife.signup.domain.usecase.SignupUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupVeganTypeViewModel @Inject constructor(
    private val signupUsecase: SignupUsecase,
) : ViewModel() {
    private val _veganTypeData = MutableLiveData<List<SignupVeganType>>()
    val veganTypeData: LiveData<List<SignupVeganType>> get() = _veganTypeData

    private val _veganTypeSelected = MutableLiveData<String>()
    val veganTypeSelected: LiveData<String> get() = _veganTypeSelected

    private val _btnBackgroundColor = MutableLiveData<Int>()
    val btnBackgroundColor: LiveData<Int> get() = _btnBackgroundColor

    fun setVeganList() {
        viewModelScope.launch {
            val result = signupUsecase.saveVeganTypeList()
            _veganTypeData.value = result
        }
    }

    fun setSelectedVeganName(name: String) {
        _veganTypeSelected.value = when(name) {
            "비건" -> "VEGAN"
            "락토" -> "LACTO"
            "오보" -> "OVO"
            "락토오보" -> "LACTO_OVO"
            "페스코" -> "PESCO"
            else -> ""
        }
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