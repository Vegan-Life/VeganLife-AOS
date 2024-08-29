package com.project.veganlife.mypage.di

import com.project.veganlife.mypage.data.datasource.ProfileModifyDataSource
import com.project.veganlife.mypage.data.datasource.ProfileModifyDataSourceImpl
import com.project.veganlife.mypage.data.repositoryImpl.ProfileModifyRepositoryImpl
import com.project.veganlife.mypage.domain.repository.ProfileModifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModifyModule {

    @Provides
    @Singleton
    fun providesProfileModifyRepository(
        profileModifyrepositoryimpl: ProfileModifyRepositoryImpl
    ): ProfileModifyRepository {
        return profileModifyrepositoryimpl
    }

    @Provides
    @Singleton
    fun providesProfileModifyDataSource(
        profileModifydatasourceimpl: ProfileModifyDataSourceImpl
    ): ProfileModifyDataSource {
        return profileModifydatasourceimpl
    }
}