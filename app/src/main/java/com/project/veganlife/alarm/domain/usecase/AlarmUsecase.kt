package com.project.veganlife.alarm.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.alarm.data.model.AlarmContent
import com.project.veganlife.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmUsecase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(): Flow<PagingData<AlarmContent>> {
        return alarmRepository.getAlarmList()
    }
}