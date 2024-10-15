package com.project.veganlife.alarm.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.alarm.data.datasource.AlarmPagingSource
import com.project.veganlife.alarm.data.model.AlarmContent
import com.project.veganlife.alarm.data.remote.AlarmApi
import com.project.veganlife.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val api: AlarmApi,
): AlarmRepository{
    override suspend fun getAlarmList(): Flow<PagingData<AlarmContent>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { AlarmPagingSource(api)}
        ).flow
    }
}