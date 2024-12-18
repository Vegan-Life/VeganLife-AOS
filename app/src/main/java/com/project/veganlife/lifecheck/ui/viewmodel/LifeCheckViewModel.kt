package com.project.veganlife.lifecheck.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckUseCase
import com.project.veganlife.lifecheck.util.EventWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LifeCheckViewModel @Inject constructor(
    private val lifeCheckUseCase: LifeCheckUseCase,
) : ViewModel() {

    // 일일 섭취량 조회
    private val _dailyIntakeData = MutableLiveData<ApiResult<DailyIntakeResponse>>()
    val dailyIntakeData: LiveData<ApiResult<DailyIntakeResponse>> = _dailyIntakeData

    // 조회 날짜
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    // 권장 섭취량 데이터
    private val _recommendedIntakeData = MutableLiveData<ApiResult<RecommendedIntakeResponse>>()
    val recommendedIntakeData: LiveData<ApiResult<RecommendedIntakeResponse>> =
        _recommendedIntakeData

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
    val monthlyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> =
        _monthlyCalorieData

    // 연간 섭취 칼로리 조회
    private val _yearlyCalorieData = MutableLiveData<ApiResult<LifeCheckWeeklyCalorieResponse>>()
    val yearlyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> = _yearlyCalorieData

    // 식사 종류 선택
    private val _selectedDietType = MutableLiveData<String>()
    val selectedDietType: LiveData<String> = _selectedDietType

    // 키워드 기반 식품 데이터 조회
    private val _mealData = MutableStateFlow<PagingData<LifeCheckMealData>>(PagingData.empty())
    val mealData: StateFlow<PagingData<LifeCheckMealData>> = _mealData

    // 식품 데이터 등록
    private val _mealDataRegister =
        MutableLiveData<EventWrapper<ApiResult<LifeCheckMealDataRequest?>>>()
    val mealDataRegister: LiveData<EventWrapper<ApiResult<LifeCheckMealDataRequest?>>> =
        _mealDataRegister

    // ID 식품데이터 조회
    private val _mealDataById = MutableLiveData<ApiResult<LifeCheckMealDataDetail>>()
    val mealDataById: LiveData<ApiResult<LifeCheckMealDataDetail>> = _mealDataById


    // 일일 섭취량 조회
    fun fetchDailyIntake(date: String) {
        viewModelScope.launch {
            _dailyIntakeData.value = lifeCheckUseCase.getDailyIntake(date)
        }
    }

    fun updateDate(date: String) {
        _selectedDate.value = date
    }

    // 권장 섭취량 조회
    fun fetchRecommendedIntake() {
        viewModelScope.launch {
            _recommendedIntakeData.value = lifeCheckUseCase.getRecommendedIntake()
        }
    }

    // 주간 섭취 칼로리 조회
    fun fetchWeeklyCalorie(startDate: String, endDate: String) {
        viewModelScope.launch {
            _weeklyCalorieData.value = lifeCheckUseCase.getWeeklyCalorie(startDate, endDate)
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
            _monthlyCalorieData.value = lifeCheckUseCase.getMonthlyCalorie(startDate)
        }
    }

    // 연간 섭취 칼로리 조회
    fun fetchYearlyCalorie(startDate: String) {
        viewModelScope.launch {
            _yearlyCalorieData.value = lifeCheckUseCase.getYearlyCalorie(startDate)
        }
    }

    // 식사 종류 선택
    fun setSelectedDietType(dietType: String) {
        _selectedDietType.value = dietType
    }

    // 키워드 기반 식품 데이터 조회
    fun searchMealData(keyword: String, ownerType: String) {
        viewModelScope.launch {
            lifeCheckUseCase.getMealDataStream(keyword, ownerType).cachedIn(viewModelScope)
                .collectLatest {
                    _mealData.value = it
                }
        }
    }

    // 식품 데이터 등록
    fun registerMealData(lifeCheckMealDataRequest: LifeCheckMealDataRequest) {
        viewModelScope.launch {
            _mealDataRegister.value =
                EventWrapper(lifeCheckUseCase.registerMealData(lifeCheckMealDataRequest))
        }
    }

    fun fetchMealDataById(id: Long) {
        viewModelScope.launch {
            _mealDataById.value = lifeCheckUseCase.getMealDataById(id)
        }
    }
}