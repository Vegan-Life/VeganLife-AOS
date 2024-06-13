package com.project.veganlife.lifecheck.di

import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMonthlyCalorieDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMonthlyCalorieDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRecommendedIntakeDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRecommendedIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckWeeklyCalorieDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckWeeklyCalorieDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckYearlyCalorieDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckYearlyCalorieDataSourceImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckDailyIntakeRepositoryImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckMonthlyCalorieRepositoryImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckRecommendedIntakeRepositoryImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckWeeklyCalorieRepositoryImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckYearlyCalorieRepositoryImpl
import com.project.veganlife.lifecheck.domain.repository.LifeCheckDailyIntakeRepository
import com.project.veganlife.lifecheck.domain.repository.LifeCheckMonthlyCalorieRepository
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRecommendedIntakeRepository
import com.project.veganlife.lifecheck.domain.repository.LifeCheckWeeklyCalorieRepository
import com.project.veganlife.lifecheck.domain.repository.LifeCheckYearlyCalorieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LifeCheckModule {

    @Provides
    @Singleton
    fun provideLifeCheckDailyIntakeRepository(
        lifeCheckDailyIntakeRepositoryImpl: LifeCheckDailyIntakeRepositoryImpl
    ): LifeCheckDailyIntakeRepository {
        return lifeCheckDailyIntakeRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckDailyIntakeDataSource(
        lifeCheckDailyIntakeDataSourceImpl: LifeCheckDailyIntakeDataSourceImpl
    ): LifeCheckDailyIntakeDataSource {
        return lifeCheckDailyIntakeDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckRecommendedIntakeRepository(
        lifeCheckRecommendedIntakeRepositoryImpl: LifeCheckRecommendedIntakeRepositoryImpl
    ): LifeCheckRecommendedIntakeRepository {
        return lifeCheckRecommendedIntakeRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckRecommendedIntakeDataSource(
        lifeCheckRecommendedIntakeDataSourceImpl: LifeCheckRecommendedIntakeDataSourceImpl
    ): LifeCheckRecommendedIntakeDataSource {
        return lifeCheckRecommendedIntakeDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckWeeklyCalorieRepository(
        lifeCheckWeeklyCalorieRepositoryImpl: LifeCheckWeeklyCalorieRepositoryImpl
    ): LifeCheckWeeklyCalorieRepository {
        return lifeCheckWeeklyCalorieRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckWeeklyCalorieDataSource(
        lifeCheckWeeklyCalorieDataSourceImpl: LifeCheckWeeklyCalorieDataSourceImpl
    ): LifeCheckWeeklyCalorieDataSource {
        return lifeCheckWeeklyCalorieDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckMonthlyCalorieRepository(
        lifeCheckMonthlyCalorieRepositoryImpl: LifeCheckMonthlyCalorieRepositoryImpl
    ): LifeCheckMonthlyCalorieRepository {
        return lifeCheckMonthlyCalorieRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckMonthlyCalorieDataSource(
        lifeCheckMonthlyCalorieDataSourceImpl: LifeCheckMonthlyCalorieDataSourceImpl
    ): LifeCheckMonthlyCalorieDataSource {
        return lifeCheckMonthlyCalorieDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckYearlyCalorieRepository(
        lifeCheckYearlyCalorieRepositoryImpl: LifeCheckYearlyCalorieRepositoryImpl
    ): LifeCheckYearlyCalorieRepository {
        return lifeCheckYearlyCalorieRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideLifeCheckYearlyCalorieDataSource(
        lifeCheckYearlyCalorieDataSourceImpl: LifeCheckYearlyCalorieDataSourceImpl
    ): LifeCheckYearlyCalorieDataSource {
        return lifeCheckYearlyCalorieDataSourceImpl
    }
}