package com.project.veganlife.lifecheck.data.ropositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.datasource.LifeCheckWeeklyCalorieDataSource
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckWeeklyCalorieRepository
import javax.inject.Inject

class LifeCheckWeeklyCalorieRepositoryImpl @Inject constructor(
    private val dataSource: LifeCheckWeeklyCalorieDataSource
) : LifeCheckWeeklyCalorieRepository {
    override suspend fun getWeeklyCalorie(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return dataSource.getWeeklyCalorie(startDate, endDate)
    }
}