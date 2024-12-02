package com.project.veganlife.home.data.repositoryImpl

import android.os.Build
import androidx.annotation.RequiresApi
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.home.data.datasource.HomeDailyIntakeGetDataSourceImpl
import com.project.veganlife.home.data.datasource.HomeRecommendedIntakeGetDataSourceImpl
import com.project.veganlife.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDailyIntakeGetDataSourceImpl: HomeDailyIntakeGetDataSourceImpl,
    private val homeRecommendedIntakeGetDataSourceImpl: HomeRecommendedIntakeGetDataSourceImpl,
): HomeRepository{
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getDailyIntake(): ApiResult<DailyIntakeResponse>? {
        return homeDailyIntakeGetDataSourceImpl.getDailyIntake()
    }

    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>? {
        return homeRecommendedIntakeGetDataSourceImpl.getRecommendedIntake()
    }
}