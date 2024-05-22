package com.project.veganlife.lifecheck.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckGetDailyIntakeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LifeCheckViewModel @Inject constructor(
    private val lifeCheckGetDailyIntakeUseCase: LifeCheckGetDailyIntakeUseCase,
) : ViewModel() {

    // 일일 섭취량 조회
    private val _dailyIntakeData = MutableLiveData<ApiResult<DailyIntakeResponse>>()
    val dailyIntakeData: LiveData<ApiResult<DailyIntakeResponse>> = _dailyIntakeData

    // 일일 섭취량 조회
    fun fetchDailyIntake(date: String) {
        viewModelScope.launch {
            _dailyIntakeData.value = lifeCheckGetDailyIntakeUseCase(date)
        }
    }
}