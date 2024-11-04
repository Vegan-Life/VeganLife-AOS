package com.project.veganlife.alarm.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.project.veganlife.alarm.data.model.AlarmContent
import com.project.veganlife.alarm.domain.usecase.AlarmUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(
    private val usecase: AlarmUsecase,
) : ViewModel() {
    private var _alarmList = MutableStateFlow<PagingData<AlarmContent>?>(null)
    val alarmList: StateFlow<PagingData<AlarmContent>?> get() = _alarmList

    fun getAlarmData() {
        viewModelScope.launch {
            try {
                usecase().collectLatest { pagingData ->
                    _alarmList.value = pagingData
                }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }
}