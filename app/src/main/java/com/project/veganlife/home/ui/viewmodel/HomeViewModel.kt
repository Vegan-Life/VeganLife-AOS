package com.project.veganlife.home.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.home.domain.usecase.HomeDailyIntakeUsecase
import com.project.veganlife.home.domain.usecase.HomeProfilePhotoUsecase
import com.project.veganlife.home.domain.usecase.HomeRecommededIntakeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeProfilePhotoUsecase: HomeProfilePhotoUsecase,
    private val homeRecommededIntakeUsecase: HomeRecommededIntakeUsecase,
    private val homeDailyIntakeUsecase: HomeDailyIntakeUsecase,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    // 닉네임
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    // 레시피 추천 닉네임
    private val _recipeNickname = MutableLiveData<String>()
    val recipeNickname: LiveData<String> get() = _recipeNickname

    // 프로필 사진
    private val _profilePhoto = MutableLiveData<String>()
    val profilePhoto: LiveData<String> get() = _profilePhoto

    // 권장 섭취량
    private val _resultRecommendedIntake = MutableLiveData<ApiResult<RecommendedIntakeResponse>>()
    val resultRecommendedIntake: LiveData<ApiResult<RecommendedIntakeResponse>> get() = _resultRecommendedIntake

    // 일일 섭취량
    private val _resultDailyIntake = MutableLiveData<ApiResult<DailyIntakeResponse>>()
    val resultDailyIntake: LiveData<ApiResult<DailyIntakeResponse>> get() = _resultDailyIntake

    // 권장 칼로리
    private val _recommendedCalorie = MutableLiveData<Int?>()
    val recommendedCalorie: LiveData<Int?> get() = _recommendedCalorie

    // 현재 칼로리
    private val _dailyCalorie = MutableLiveData<Int?>()
    val dailyCalorie: LiveData<Int?> get() = _dailyCalorie

    // 잔여 칼로리
    val restCalorie: LiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(recommendedCalorie) { recCal ->
            val dailyCal = dailyCalorie.value ?: 0 // dailyCalorie의 값이 null이 아니면 그 값을, null이면 0을 사용
            val rest = recCal!! - dailyCal
            value = if (rest < 0) -rest else rest // 음수일 경우 절댓값으로 변환
        }
        addSource(dailyCalorie) { dCal ->
            val recCal = recommendedCalorie.value ?: 0 // recommendedCalorie의 값이 null이 아니면 그 값을, null이면 0을 사용
            val rest = recCal - dCal!!
            value = if (rest < 0) -rest else rest // 음수일 경우 절댓값으로 변환
        }
    }

    // 잔여 칼로리 비율
    val restCaloriePercent: LiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(recommendedCalorie) { recCal ->
            val dailyCal = dailyCalorie.value ?: 0 // dailyCalorie의 값이 null이 아니면 그 값을, null이면 0을 사용
            if (recCal != 0) {
                value = (dailyCal * 100) / recCal!!
            } else {
                value = 0 // recommendedCalorie가 0이면 percent를 0으로 설정
            }
        }
        addSource(dailyCalorie) { dCal ->
            val recCal = recommendedCalorie.value ?: 1 // recommendedCalorie의 값이 null이 아니면 그 값을, null이면 1을 사용하여 0으로 나누는 오류를 방지
            if (recCal != 0) {
                if (dCal != null) {
                    value = (dCal * 100) / recCal
                }
            } else {
                value = 0 // recommendedCalorie가 0이면 percent를 0으로 설정
            }
        }
    }

    // 잔여 칼로리 색상
    private val _restCalorieColor = MutableLiveData<Int>()
    val restCalorieColor: LiveData<Int> get() = _restCalorieColor

    // 잔여 칼로리 텍스트
    private val _restCalorieText = MutableLiveData<Int>()
    val restCalorieText: LiveData<Int> get() = _restCalorieText

    // 권장 탄수화물
    private val _recommendedCarbs = MutableLiveData<Int>()
    val recommendedCarbs: LiveData<Int> get() = _recommendedCarbs

    // 현재 탄수화물
    private val _carbs = MutableLiveData<Int>()
    val carbs: LiveData<Int> get() = _carbs

    // 잔여 탄수화물
    private val _restCarbs = MutableLiveData<Int>()
    val restCarbs: LiveData<Int> get() = _restCarbs

    // 탄수화물 색상
    private val _carbsBackgroundColor = MutableLiveData<Int>()
    val carbsBackgroundColor: LiveData<Int> get() = _carbsBackgroundColor

    // 권장 단백질
    private val _recommendedProtein = MutableLiveData<Int>()
    val recommendedProtein: LiveData<Int> get() = _recommendedProtein

    // 현재 단밸질
    private val _protein = MutableLiveData<Int>()
    val protein: LiveData<Int> get() = _protein

    // 잔여 단백질
    private val _restProtein = MutableLiveData<Int>()
    val restProtein: LiveData<Int> get() = _restProtein


    // 단백질 색상
    private val _proteinBackgroundColor = MutableLiveData<Int>()
    val proteinBackgroundColor: LiveData<Int> get() = _proteinBackgroundColor

    // 권장 지방
    private val _recommendedFat = MutableLiveData<Int>()
    val recommendedFat: LiveData<Int> get() = _recommendedFat

    // 현재 지방
    private val _fat = MutableLiveData<Int>()
    val fat: LiveData<Int> get() = _fat

    // 잔여 지방
    private val _restFat = MutableLiveData<Int>()
    val restFat: LiveData<Int> get() = _restFat

    // 지방 색상
    private val _fatBackgroundColor = MutableLiveData<Int>()
    val fatBackgroundColor: LiveData<Int> get() = _fatBackgroundColor

    fun getNickName() {
        _nickname.value = sharedPreferences.getString("Nickname", "") + "님,"
        _recipeNickname.value = sharedPreferences.getString("Nickname", "")
    }

    fun getProfilePhoto() {
        viewModelScope.launch {
            _profilePhoto.value = homeProfilePhotoUsecase.invoke()
            // JWT 토큰이 만료되었습니다. 시에 토큰 재발급 함수 생성
        }
    }

    fun getRecommendedIntake() {
        viewModelScope.launch {
            _resultRecommendedIntake.value = homeRecommededIntakeUsecase.invoke()
        }
    }

    fun getDailyIntake() {
        viewModelScope.launch {
            _resultDailyIntake.value = homeDailyIntakeUsecase.invoke()
        }
    }

    fun restIntakeCalculate() {
        if(recommendedCarbs.value != null && carbs.value != null) {
            _restCarbs.value = recommendedCarbs.value!! - carbs.value!!
        }

        if(recommendedProtein.value != null && protein.value != null) {
            _restProtein.value = recommendedProtein.value!! - protein.value!!
        }

        if(recommendedFat.value != null && fat.value != null) {
            _restFat.value = recommendedFat.value!! - fat.value!!
        }

    }

    fun setRecommendedIntakeValue(response: RecommendedIntakeResponse) {
        _recommendedCalorie.value = response.dailyCalorie
        _recommendedCarbs.value = response.dailyCarbs
        _recommendedProtein.value = response.dailyProtein
        _recommendedFat.value = response.dailyFat

        restIntakeCalculate()
        setIntakeColorState()
    }

    fun setDailyIntkeValue(response: DailyIntakeResponse) {
        _dailyCalorie.value = response.calorie
        _carbs.value = response.carbs
        _protein.value = response.protein
        _fat.value = response.fat

        restIntakeCalculate()
        setIntakeColorState()
    }

    fun setRestCalorieColor() {
        if (recommendedCalorie.value != null && dailyCalorie.value != null) {
            if (recommendedCalorie.value!! < dailyCalorie.value!!) {
                _restCalorieColor.value = R.color.no
                _restCalorieText.value = R.string.all_over_rest_kcal
            } else {
                _restCalorieColor.value = R.color.base3
                _restCalorieText.value = R.string.all_rest_kcal
            }
        }
    }

    fun setIntakeColorState() {
        if(restCarbs.value != null) {
            if(restCarbs.value!! < 0) {
                _carbsBackgroundColor.value = R.color.no
            } else {
                _carbsBackgroundColor.value = R.color.base3
            }
        }

        if(restProtein.value != null) {
            if(restProtein.value!! < 0) {
                _proteinBackgroundColor.value = R.color.no
            } else {
                _proteinBackgroundColor.value = R.color.base1
            }
        }

        if(restFat.value != null) {
            if(restFat.value!! < 0) {
                _fatBackgroundColor.value = R.color.no
            } else {
                _fatBackgroundColor.value = R.color.point1
            }
        }
    }
}