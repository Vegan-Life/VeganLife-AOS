package com.project.veganlife.home.di

import com.project.veganlife.home.data.datasource.HomeRecommendedIntakeGetDataSource
import com.project.veganlife.home.data.datasource.HomeRecommendedIntakeGetDataSourceImpl
import com.project.veganlife.home.data.repositoryImpl.HomeRecommendedIntakeGetRepositoryImpl
import com.project.veganlife.home.domain.repository.HomeRecommendedIntakeGetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeRecommendedIntakeModule {

    @Provides
    @Singleton
    fun providesHomeRecommendedIntakeRepository(
        homeRecommendedIntakeGetRepositoryImpl: HomeRecommendedIntakeGetRepositoryImpl
    ): HomeRecommendedIntakeGetRepository {
        return homeRecommendedIntakeGetRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesHomeRecommendedIntakeDataSource(
        homeRecommendedIntakeGetDataSourceImpl: HomeRecommendedIntakeGetDataSourceImpl
    ): HomeRecommendedIntakeGetDataSource {
        return homeRecommendedIntakeGetDataSourceImpl
    }
}