package com.project.veganlife.lifecheck.data.ropositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckDeleteMealDataDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMealDataByIdDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMealDataPagingSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMealLogListDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckModifyMealDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMonthlyCalorieDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRecommendedIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRegisterMealDataDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRegisterMealLogDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckWeeklyCalorieDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckYearlyCalorieDataSourceImpl
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogListResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class LifeCheckRepositoryImpl @Inject constructor(
    private val lifeCheckApi: LifeCheckApi,
    private val lifeCheckDailyDataSource: LifeCheckDailyIntakeDataSourceImpl,
    private val lifeCheckMonthlyCalorieDataSource: LifeCheckMonthlyCalorieDataSourceImpl,
    private val lifeCheckRecommendedIntakeDataSource: LifeCheckRecommendedIntakeDataSourceImpl,
    private val lifeCheckMealDataDataSource: LifeCheckRegisterMealDataDataSourceImpl,
    private val lifeCheckWeeklyDataSource: LifeCheckWeeklyCalorieDataSourceImpl,
    private val lifeCheckYearlyDataSource: LifeCheckYearlyCalorieDataSourceImpl,
    private val lifeCheckMealDataByIdDataSource: LifeCheckMealDataByIdDataSourceImpl,
    private val lifeCheckModifyMealDataSource: LifeCheckModifyMealDataSourceImpl,
    private val lifeCheckDeleteMealDataDataSource: LifeCheckDeleteMealDataDataSourceImpl,
    private val lifeCheckRegisterMealLogDataSource: LifeCheckRegisterMealLogDataSourceImpl,
    private val lifeCheckMealLogListDataSource: LifeCheckMealLogListDataSourceImpl,
) : LifeCheckRepository {
    override suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse> {
        return lifeCheckDailyDataSource.getDailyIntake(date)
    }

    override suspend fun getMealDataStream(
        keyword: String,
        ownerType: String
    ): Flow<PagingData<LifeCheckMealData>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                LifeCheckMealDataPagingSource(
                    lifeCheckApi,
                    keyword,
                    ownerType,
                )
            }
        ).flow
    }

    override suspend fun getMonthlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return lifeCheckMonthlyCalorieDataSource.getMonthlyCalorie(startDate)
    }

    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse> {
        return lifeCheckRecommendedIntakeDataSource.getRecommendedIntake()
    }

    override suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<Unit> {
        return lifeCheckMealDataDataSource.registerMealData(mealData)
    }

    override suspend fun getWeeklyCalorie(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return lifeCheckWeeklyDataSource.getWeeklyCalorie(startDate, endDate)
    }

    override suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return lifeCheckYearlyDataSource.getYearlyCalorie(startDate)
    }

    override suspend fun getMealDataById(id: Long): ApiResult<LifeCheckMealDataDetail> {
        return lifeCheckMealDataByIdDataSource.getMealDataById(id)
    }

    override suspend fun modifyMealData(
        id: Long,
        request: LifeCheckMealDataRequest
    ): ApiResult<Unit> {
        return lifeCheckModifyMealDataSource.modifyMealData(id, request)
    }

    override suspend fun deleteMealData(id: Long): ApiResult<Unit> {
        return lifeCheckDeleteMealDataDataSource.deleteMealData(id)
    }

    override suspend fun registerMealLog(
        mealLogRequest: RequestBody,
        images: List<MultipartBody.Part>?
    ): ApiResult<Unit> {
        return lifeCheckRegisterMealLogDataSource.registerMealLog(mealLogRequest, images)
    }

    override suspend fun getMealLogList(date: String): ApiResult<List<LifeCheckMealLogListResponse>> {
        return lifeCheckMealLogListDataSource.getMealLogList(date)
    }
}