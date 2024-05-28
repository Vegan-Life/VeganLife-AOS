package com.project.veganlife.lifecheck.data.ropositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSource
import com.project.veganlife.lifecheck.domain.repository.LifeCheckDailyIntakeRepository
import javax.inject.Inject

class LifeCheckDailyIntakeRepositoryImpl @Inject constructor(
    private val dataSource: LifeCheckDailyIntakeDataSource,
) : LifeCheckDailyIntakeRepository {
    override suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse> {
        return dataSource.getDailyIntake(date)
    }
}