package com.project.veganlife.home.di

import com.project.veganlife.home.data.datasource.HomeDailyIntakeGetDataSource
import com.project.veganlife.home.data.datasource.HomeDailyIntakeGetDataSourceImpl
import com.project.veganlife.home.data.repositoryImpl.HomeDailyIntakeGetRepositoryImpl
import com.project.veganlife.home.domain.repository.HomeDailyIntakeGetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeDailyIntakeModule {

    @Provides
    @Singleton
    fun providesHomeDailyIntakeRepository(
        homeDailyIntakeGetRepositoryImpl: HomeDailyIntakeGetRepositoryImpl,
    ): HomeDailyIntakeGetRepository {
        return homeDailyIntakeGetRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesHomeDailyIntakeDataSource(
        homeDailyIntakeGetDataSourceImpl: HomeDailyIntakeGetDataSourceImpl
    ): HomeDailyIntakeGetDataSource {
        return homeDailyIntakeGetDataSourceImpl
    }
}