package com.project.veganlife.signup.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.R
import com.project.veganlife.signup.data.model.SignupRequest
import com.project.veganlife.signup.domain.SignupAddInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupAddInfoViewModel @Inject constructor(
    private val signupAddInfoUsecase: SignupAddInfoUsecase,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    // 닉네임
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname
    private val NICKNAME_PATTERN = "[가-힣]{2,10}".toRegex()

    // 성별
    private val _isMaleSelected = MutableLiveData<Boolean>()
    val isMaleSelected: LiveData<Boolean?> get() = _isMaleSelected

    private val _isFemaleSelected = MutableLiveData<Boolean>()
    val isFemaleSelected: LiveData<Boolean?> get() = _isFemaleSelected

    // 여성 성별 상태에 따른 배경색과 글씨색
    private val _femaleBackgroundColor = MutableLiveData<Int>()
    val femaleBackgroundColor: LiveData<Int> get() = _femaleBackgroundColor

    private val _femaleTextColor = MutableLiveData<Int>()
    val femaleTextColor: LiveData<Int> get() = _femaleTextColor

    // 남성 성별 상태에 따른 배경색과 글씨색
    private val _maleBackgroundColor = MutableLiveData<Int>()
    val maleBackgroundColor: LiveData<Int> get() = _maleBackgroundColor

    private val _maleTextColor = MutableLiveData<Int>()
    val maleTextColor: LiveData<Int> get() = _maleTextColor

    // 비건 타입
    private val _vegenType = MutableLiveData<String?>()
    val vegenType: LiveData<String?> get() = _vegenType

    // 출생연도
    private val _birthYear = MutableLiveData<Int>()
    val birthYear: LiveData<Int> get() = _birthYear

    // 키
    private val _height = MutableLiveData<Int>()
    val height: LiveData<Int> get() = _height

    // 몸무게
    private val _weight = MutableLiveData<Int>()
    val weight: LiveData<Int> get() = _weight

    // 추가 정보 입력
    private val _signupRespone = MutableLiveData<String?>()
    val signupRespone: LiveData<String?> get() = _signupRespone

    // 에러 메세지
    private val _responeMessage = MutableLiveData<String?>()
    val responeMessage: LiveData<String?> get() = _responeMessage

    // 모든 상태 체크
    private val _allStateCheck = MutableLiveData<Boolean>()
    val allStateCheck: LiveData<Boolean> get() = _allStateCheck

    // 닉네임 조건식
    fun isNicknameValid(nickname: String): Boolean {
        return nickname.matches(NICKNAME_PATTERN)
    }

    // 여성 버튼 선택 시
    fun onFemaleSelected() {
        _isFemaleSelected.value = true
        _isMaleSelected.value = false // 남성 선택 해제

        setGenderButtonColor(isFemaleSelected.value, isMaleSelected.value)
    }

    // 남성 버튼 선택 시
    fun onMaleSelected() {
        _isMaleSelected.value = true
        _isFemaleSelected.value = false // 여성 선택 해제

        setGenderButtonColor(isFemaleSelected.value, isMaleSelected.value)
    }

    // 버튼 상태에 따른 색상 변경
    fun setGenderButtonColor(female: Boolean?, male: Boolean?) {
        if (female == true) {
            _femaleBackgroundColor.value = R.drawable.signup_vegan_type_selected
            _femaleTextColor.value = android.R.color.white
        } else {
            _femaleBackgroundColor.value = R.drawable.signup_vegan_type
            _femaleTextColor.value = R.color.gray3
        }

        if (male == true) {
            _maleBackgroundColor.value = R.drawable.signup_vegan_type_selected
            _maleTextColor.value = android.R.color.white
        } else {
            _maleBackgroundColor.value = R.drawable.signup_vegan_type
            _maleTextColor.value = R.color.gray3
        }
    }

    // 비건 타입에 따른 값 변경
    fun isVegetarianTypeValid(veganType: String): String {
        var str = ""
        when (veganType) {
            "비건" -> str = "VEGAN"
            "락토" -> str = "LACTO"
            "오보" -> str = "OVO"
            "락토오보" -> str = "LACTO_OVO"
            "페스코" -> str = "PESCO"
        }
        return str
    }

    // 출생연도 조건식
    private fun isBirthYearValid(value: Int?): Boolean {
        return value != null && value > 1900 && value <= 2999
    }

    // 키 조건식
    private fun isHeightValid(value: Int?): Boolean {
        return value != null && value > 0 && value <= 200
    }

    // 몸무게 조건식
    private fun isWeightValid(value: Int?): Boolean {
        return value != null && value > 0 && value <= 100
    }

    // 닉네임 setter
    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    // 비건 타입 setter
    fun setVeganType(vegantype: String) {
        _vegenType.value = vegantype
    }

    // 출생연도 setter
    fun setBirthyear(birthyear: Int) {
        _birthYear.value = birthyear
    }

    // 키 setter
    fun setHeight(height: Int) {
        _height.value = height
    }

    // 몸무게 setter
    fun setWeight(weight: Int) {
        _weight.value = weight
    }

    fun setSignupResponse(response: String) {
        _signupRespone.value = response
    }

    fun isAllStateValid() {
        val isNicknameValid = isNicknameValid(nickname.value ?: "")
        val isGenderSelectedValid = isMaleSelected.value == true || isFemaleSelected.value == true
        val isBirthYearValid = isBirthYearValid(birthYear.value)
        val isHeightValid = isHeightValid(height.value)
        val isWeightValid = isWeightValid(weight.value)

        _allStateCheck.value =
            isNicknameValid && isGenderSelectedValid && isBirthYearValid && isHeightValid && isWeightValid
    }

    fun setSignupRequest(state: Boolean?) {
        if (state == true) {
            val vegetarianType = isVegetarianTypeValid(vegenType.value.toString())
            val gender =
                if (isMaleSelected.value == true) "M" else if (isFemaleSelected.value == true) "F" else ""

            val signupRequest = SignupRequest(
                nickname = nickname.value ?: "",
                gender = gender,
                vegetarianType = vegetarianType,
                birthYear = birthYear.value ?: 0,
                height = height.value ?: 0,
                weight = weight.value ?: 0
            )
            viewModelScope.launch {
                _signupRespone.value = signupAddInfoUsecase(signupRequest)
            }
        } else {
            _signupRespone.value = "모든 정보를 올바르게 입력해주세요"
        }

        if(signupRespone.value == nickname.value) {
            setSaveNickname(signupRespone.value)
        }
    }

    fun setSaveNickname(message: String?) {
        sharedPreferences.edit().putString("Nickname", message).apply()
    }

    fun setNextButtonBackgroundColor(allState: Boolean): Int {
        return if (allState == true) R.color.base3 else R.color.gray3
    }
}