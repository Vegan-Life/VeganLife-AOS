package com.project.veganlife.mypage.di

import com.project.veganlife.mypage.data.repositoryImpl.MypageScrapedRecipeRepositoryImpl
import com.project.veganlife.mypage.domain.repository.MypageScrapedRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MypageGetScrapedRecipeModule {

    @Provides
    @Singleton
    fun providesMypageGetScrapedRecipeRepository(
        myScrapedRecipeRepositoryImpl: MypageScrapedRecipeRepositoryImpl
    ): MypageScrapedRecipeRepository {
        return myScrapedRecipeRepositoryImpl
    }
}