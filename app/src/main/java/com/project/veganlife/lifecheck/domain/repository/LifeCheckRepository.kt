package com.project.veganlife.lifecheck.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import kotlinx.coroutines.flow.Flow

interface LifeCheckRepository {

    suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse>

    suspend fun getMealDataStream(keyword: String, ownerType: String): Flow<PagingData<LifeCheckMealData>>

    suspend fun getMonthlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse>

    suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>

    suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<Unit>

    suspend fun getWeeklyCalorie(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse>

    suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse>

    suspend fun getMealDataById(id: Long): ApiResult<LifeCheckMealDataDetail>

    suspend fun modifyMealData(id: Long, request: LifeCheckMealDataRequest): ApiResult<Unit>

    suspend fun deleteMealData(id: Long): ApiResult<Unit>
}