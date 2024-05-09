package com.project.veganlife.home.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.home.data.datasource.HomeDailyIntakeGetDataSource
import com.project.veganlife.home.domain.repository.HomeDailyIntakeGetRepository
import javax.inject.Inject

class HomeDailyIntakeGetRepositoryImpl @Inject constructor(
    private val homeDailyIntakeGetDataSource: HomeDailyIntakeGetDataSource
): HomeDailyIntakeGetRepository{
    override suspend fun getDailyIntake(): ApiResult<DailyIntakeResponse>? {
        return homeDailyIntakeGetDataSource.getDailyIntake()
    }
}