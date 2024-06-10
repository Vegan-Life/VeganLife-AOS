package com.project.veganlife.lifecheck.data.ropositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMonthlyCalorieDataSource
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckMonthlyCalorieRepository
import javax.inject.Inject

class LifeCheckMonthlyCalorieRepositoryImpl @Inject constructor(
    private val dataSource: LifeCheckMonthlyCalorieDataSource
) : LifeCheckMonthlyCalorieRepository {
    override suspend fun getMonthlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return dataSource.getMonthlyCalorie(startDate)
    }
}