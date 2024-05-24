package com.project.veganlife.di

import com.project.veganlife.data.datasource.ProfileGetDataSource
import com.project.veganlife.data.datasource.ProfileGetDataSourceImpl
import com.project.veganlife.data.repositoryImpl.ProfileGetRepositoryImpl
import com.project.veganlife.domain.repository.ProfileGetRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileGetModule {

    @Provides
    @Singleton
    fun providesProfileGetRepository(
        profileGetRepositoryImpl: ProfileGetRepositoryImpl,
    ): ProfileGetRespository {
        return profileGetRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesProfileGetDataSource(
        profileGetDataSourceImpl: ProfileGetDataSourceImpl
    ): ProfileGetDataSource {
        return profileGetDataSourceImpl
    }
}