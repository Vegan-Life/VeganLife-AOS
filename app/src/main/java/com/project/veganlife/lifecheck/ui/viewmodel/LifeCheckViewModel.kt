package com.project.veganlife.lifecheck.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckGetDailyIntakeUseCase
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckGetMonthlyCalorieUseCase
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckGetRecommendedIntakeUseCase
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckGetWeeklyCalorieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LifeCheckViewModel @Inject constructor(
    private val lifeCheckGetDailyIntakeUseCase: LifeCheckGetDailyIntakeUseCase,
    private val lifeCheckGetRecommendedIntakeUseCase: LifeCheckGetRecommendedIntakeUseCase,
    private val lifeCheckGetWeeklyCalorieUseCase: LifeCheckGetWeeklyCalorieUseCase,
    private val lifeCheckGetMonthlyCalorieUseCase: LifeCheckGetMonthlyCalorieUseCase,
) : ViewModel() {

    // 일일 섭취량 조회
    private val _dailyIntakeData = MutableLiveData<ApiResult<DailyIntakeResponse>>()
    val dailyIntakeData: LiveData<ApiResult<DailyIntakeResponse>> = _dailyIntakeData

    // 조회 날짜
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    // 권장 섭취량 데이터
    private val _recommendedIntakeData = MutableLiveData<ApiResult<RecommendedIntakeResponse>>()
    val recommendedIntakeData: LiveData<ApiResult<RecommendedIntakeResponse>> = _recommendedIntakeData

    // 주간 섭취 칼로리 조회
    private val _weeklyCalorieData = MutableLiveData<ApiResult<LifeCheckWeeklyCalorieResponse>>()
    val weeklyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> = _weeklyCalorieData

    // 조회 기간 설정
    private val _selectedWeeklyStartDate = MutableLiveData<String>()
    val selectedWeeklyStartDate: LiveData<String> = _selectedWeeklyStartDate

    private val _selectedWeeklyEndDate = MutableLiveData<String>()
    val selectedWeeklyEndDate: LiveData<String> = _selectedWeeklyEndDate

    // 월간 섭취 칼로리 조회
    private val _monthlyCalorieData = MutableLiveData<ApiResult<LifeCheckWeeklyCalorieResponse>>()
    val monthlyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> = _monthlyCalorieData

    // 일일 섭취량 조회
    fun fetchDailyIntake(date: String) {
        viewModelScope.launch {
            _dailyIntakeData.value = lifeCheckGetDailyIntakeUseCase(date)
        }
    }

    fun updateDate(date: String) {
        _selectedDate.value = date
    }

    // 권장 섭취량 조회
    fun fetchRecommendedIntake() {
        viewModelScope.launch {
            _recommendedIntakeData.value = lifeCheckGetRecommendedIntakeUseCase()
        }
    }

    // 주간 섭취 칼로리 조회
    fun fetchWeeklyCalorie(startDate: String, endDate: String) {
        viewModelScope.launch {
            _weeklyCalorieData.value = lifeCheckGetWeeklyCalorieUseCase(startDate, endDate)
        }
    }

    fun updateWeeklyStartDate(date: String) {
        _selectedWeeklyStartDate.value = date
    }

    fun updateWeeklyEndDate(date: String) {
        _selectedWeeklyEndDate.value = date
    }

    // 월간 섭취 칼로리 조회
    fun fetchMonthlyCalorie(startDate: String) {
        viewModelScope.launch {
            _monthlyCalorieData.value = lifeCheckGetMonthlyCalorieUseCase(startDate)
        }
    }

}