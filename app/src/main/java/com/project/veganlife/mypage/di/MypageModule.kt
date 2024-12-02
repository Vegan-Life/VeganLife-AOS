package com.project.veganlife.mypage.di

import com.project.veganlife.mypage.data.repositoryImpl.MypageRepositoryImpl
import com.project.veganlife.mypage.domain.repository.MypageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MypageModule {

    @Provides
    @Singleton
    fun providesMypageGetPostedListRepository(
        mypageRepositoryImpl: MypageRepositoryImpl
    ): MypageRepository {
        return mypageRepositoryImpl
    }
}