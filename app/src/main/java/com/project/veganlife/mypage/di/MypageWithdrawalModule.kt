package com.project.veganlife.mypage.di

import com.project.veganlife.mypage.data.datasource.MypageWithdrawalDataSource
import com.project.veganlife.mypage.data.datasource.MypageWithdrawalDataSourceImpl
import com.project.veganlife.mypage.data.repositoryImpl.MypageWithdrawalRepositoryImpl
import com.project.veganlife.mypage.domain.repository.MypageWithdrawalRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MypageWithdrawalModule {

    @Provides
    @Singleton
    fun providesMypageGetUserInfoRepository(
        mypageWithdrawalRepositoryImpl: MypageWithdrawalRepositoryImpl
    ): MypageWithdrawalRespository {
        return mypageWithdrawalRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesMypageGetUserInfoDataSource(
        mypageWithdrawalDataSourceImpl: MypageWithdrawalDataSourceImpl
    ): MypageWithdrawalDataSource {
        return mypageWithdrawalDataSourceImpl
    }
}