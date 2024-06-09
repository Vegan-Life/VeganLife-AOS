package com.project.veganlife.mypage.di

import com.project.veganlife.mypage.data.repositoryImpl.MypagePostedRepositoryImpl
import com.project.veganlife.mypage.domain.repository.MypagePostedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MypageGetPostedModule {

    @Provides
    @Singleton
    fun providesMypageGetPostedListRepository(
        mypagePostedRepositoryImpl: MypagePostedRepositoryImpl
    ): MypagePostedRepository {
        return mypagePostedRepositoryImpl
    }
}