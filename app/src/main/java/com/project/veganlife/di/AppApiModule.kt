package com.project.veganlife.di

import com.project.veganlife.data.remote.DailyIntakeGetApi
import com.project.veganlife.data.remote.ProfileInfoGetApi
import com.project.veganlife.data.remote.RecommendedIntakeGetApi
import com.project.veganlife.mypage.data.remote.MypageGetUserInfoApi
import com.project.veganlife.mypage.data.remote.MypageWithDrawalApi
import com.project.veganlife.signup.data.remote.SignupApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class AppApiModule {
    @Provides
    fun provideSignupApi(retrofit: Retrofit): SignupApi {
        return retrofit.create(SignupApi::class.java)
    }

    @Provides
    fun provideHomeProfilePhotoApi(retrofit: Retrofit): ProfileInfoGetApi{
        return retrofit.create(ProfileInfoGetApi::class.java)
    }

    @Provides
    fun provideRecommendedIntakeGetApi(retrofit: Retrofit): RecommendedIntakeGetApi{
        return retrofit.create(RecommendedIntakeGetApi::class.java)
    }

    @Provides
    fun provideDailyIntakeGetApi(retrofit: Retrofit): DailyIntakeGetApi{
        return retrofit.create(DailyIntakeGetApi::class.java)
    }

    @Provides
    fun provideMypageGetUserInfoApi(retrofit: Retrofit): MypageGetUserInfoApi{
        return retrofit.create(MypageGetUserInfoApi::class.java)
    }

    @Provides
    fun provideMypageDeleteWithdrawalApi(retrofit: Retrofit): MypageWithDrawalApi{
        return retrofit.create(MypageWithDrawalApi::class.java)
    }
}