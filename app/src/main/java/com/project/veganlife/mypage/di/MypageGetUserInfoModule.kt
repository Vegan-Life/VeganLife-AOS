package com.project.veganlife.mypage.di

import com.project.veganlife.mypage.data.datasource.MypageGetUserInfoDataSource
import com.project.veganlife.mypage.data.datasource.MypageGetUserInfoDataSourceImpl
import com.project.veganlife.mypage.data.repositoryImpl.MypageGetUserInfoRepositoryImpl
import com.project.veganlife.mypage.domain.repository.MypageGetUserRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MypageGetUserInfoModule {

    @Provides
    @Singleton
    fun providesMypageGetUserInfoRepository(
        mypageGetUserInfoRepositoryImpl: MypageGetUserInfoRepositoryImpl
    ): MypageGetUserRespository {
        return mypageGetUserInfoRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesMypageGetUserInfoDataSource(
        mypageGetUserInfoDataSourceImpl: MypageGetUserInfoDataSourceImpl
    ): MypageGetUserInfoDataSource {
        return mypageGetUserInfoDataSourceImpl
    }
}