package com.project.veganlife.home.di

import com.project.veganlife.home.data.datasource.HomeHomeProfilePhotoGetDataSourceImpl
import com.project.veganlife.home.data.datasource.HomeProfilePhotoGetDataSource
import com.project.veganlife.home.data.repositoryImpl.HomeProfilePhotoGetRepositoryImpl
import com.project.veganlife.home.domain.repository.HomeProfilePhotoGetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeProfilePhotoModule {

    @Provides
    @Singleton
    fun providesHomeProfilePhotoRepository(
        homeProfilePhotoGetRepositoryImpl: HomeProfilePhotoGetRepositoryImpl
    ): HomeProfilePhotoGetRepository {
        return homeProfilePhotoGetRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesHomeProfilePhotoDataSource(
        homeProfilePhotoGetDataSourceImpl: HomeHomeProfilePhotoGetDataSourceImpl
    ): HomeProfilePhotoGetDataSource {
        return homeProfilePhotoGetDataSourceImpl
    }
}