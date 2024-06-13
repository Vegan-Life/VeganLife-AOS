package com.project.veganlife.lifecheck.data.ropositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.datasource.LifeCheckYearlyCalorieDataSource
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckYearlyCalorieRepository
import javax.inject.Inject

class LifeCheckYearlyCalorieRepositoryImpl @Inject constructor(
    private val dataSource: LifeCheckYearlyCalorieDataSource
) : LifeCheckYearlyCalorieRepository {
    override suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return dataSource.getYearlyCalorie(startDate)
    }
}