package com.project.veganlife.alarm.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.project.veganlife.utils.SseClient
import com.project.veganlife.utils.ui.AlarmIconUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val sseClient: SseClient,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    fun checkAlarm() {
        sseClient.subscribeSse {
            sharedPreferences.edit().putInt("alarm", 1).apply()
        }
    }

    fun closeAlarm() {
        sharedPreferences.edit().putInt("alarm", 0).apply()
    }

    fun alarmStateCheck(): Int {
        Log.d("alarm 값",sharedPreferences.getInt("alarm",0).toString())
        return AlarmIconUtils.alarmIconUiChange(sharedPreferences)
    }

}