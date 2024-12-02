package com.project.veganlife.home.di

import com.project.veganlife.home.data.repositoryImpl.HomeRepositoryImpl
import com.project.veganlife.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providesHomeDailyIntakeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository {
        return homeRepositoryImpl
    }
}