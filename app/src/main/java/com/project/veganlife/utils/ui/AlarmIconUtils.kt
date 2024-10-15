package com.project.veganlife.utils.ui

import android.content.SharedPreferences
import com.project.veganlife.R

class AlarmIconUtils {
    companion object {
        fun alarmIconUiChange(sharedPreferences: SharedPreferences): Int {
            val alarm = sharedPreferences.getInt("alarm",0)
            var result = 0
            if(alarm == 1) result = R.drawable.all_recieved_alarm
            else result = R.drawable.all_alarm

            return result
        }
    }
}