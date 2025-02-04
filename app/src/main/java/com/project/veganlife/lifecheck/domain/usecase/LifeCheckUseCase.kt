package com.project.veganlife.lifecheck.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogListResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LifeCheckUseCase @Inject constructor(
    private val lifeCheckRepository: LifeCheckRepository,
) {
    suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse> {
        return lifeCheckRepository.getDailyIntake(date)
    }

    suspend fun getMealDataStream(
        keyword: String,
        ownerType: String
    ): Flow<PagingData<LifeCheckMealData>> {
        return lifeCheckRepository.getMealDataStream(keyword, ownerType)
    }

    suspend fun getMonthlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return lifeCheckRepository.getMonthlyCalorie(startDate)
    }

    suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse> {
        return lifeCheckRepository.getRecommendedIntake()
    }

    suspend fun getWeeklyCalorie(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return lifeCheckRepository.getWeeklyCalorie(startDate, endDate)
    }

    suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return lifeCheckRepository.getYearlyCalorie(startDate)
    }

    suspend fun registerMealData(data: LifeCheckMealDataRequest): ApiResult<Unit> {
        return lifeCheckRepository.registerMealData(data)
    }

    suspend fun getMealDataById(id: Long): ApiResult<LifeCheckMealDataDetail> {
        return lifeCheckRepository.getMealDataById(id)
    }

    suspend fun modifyMealData(mealId: Long, request: LifeCheckMealDataRequest): ApiResult<Unit> {
        return lifeCheckRepository.modifyMealData(mealId, request)
    }

    suspend fun deleteMealData(id: Long): ApiResult<Unit> {
        return lifeCheckRepository.deleteMealData(id)
    }

    suspend fun registerMealLog(
        mealLogRequest: RequestBody,
        images: List<MultipartBody.Part>?
    ): ApiResult<Unit> {
        return lifeCheckRepository.registerMealLog(mealLogRequest, images)
    }

    suspend fun getMealLogList(date: String): ApiResult<List<LifeCheckMealLogListResponse>> {
        return lifeCheckRepository.getMealLogList(date)
    }
}