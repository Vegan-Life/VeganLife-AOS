package com.project.veganlife.alarm.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.alarm.data.model.AlarmContent
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarmList(): Flow<PagingData<AlarmContent>>
}