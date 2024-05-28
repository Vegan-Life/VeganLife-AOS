package com.project.veganlife.lifecheck.di

import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRecommendedIntakeDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRecommendedIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckDailyIntakeRepositoryImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckRecommendedIntakeRepositoryImpl
import com.project.veganlife.lifecheck.domain.repository.LifeCheckDailyIntakeRepository
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRecommendedIntakeRepository
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
}