package com.project.veganlife.lifecheck.di

import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSource
import com.project.veganlife.lifecheck.data.datasource.LifeCheckDailyIntakeDataSourceImpl
import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckDailyIntakeRepositoryImpl
import com.project.veganlife.lifecheck.domain.repository.LifeCheckDailyIntakeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LifeCheckDailyIntakeModule {

    @Provides
    fun provideLifeCheckDailyIntakeRepository(
        lifeCheckDailyIntakeRepositoryImpl: LifeCheckDailyIntakeRepositoryImpl
    ): LifeCheckDailyIntakeRepository {
        return lifeCheckDailyIntakeRepositoryImpl
    }

    @Provides
    fun provideLifeCheckDailyIntakeDataSource(
        lifeCheckDailyIntakeDataSourceImpl: LifeCheckDailyIntakeDataSourceImpl
    ): LifeCheckDailyIntakeDataSource {
        return lifeCheckDailyIntakeDataSourceImpl
    }
}