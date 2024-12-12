package com.project.veganlife.di

import com.project.veganlife.alarm.data.remote.AlarmApi
import com.project.veganlife.community.data.remote.CommunityApi
import com.project.veganlife.data.remote.IntakeGetApi
import com.project.veganlife.data.remote.ProfileInfoGetApi
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import com.project.veganlife.login.data.model.remote.LoginApi
import com.project.veganlife.mypage.data.remote.MypageApi
import com.project.veganlife.recipe.data.remote.RecipeApi
import com.project.veganlife.signup.data.remote.SignupApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppApiModule {
    @Provides
    @Singleton
    fun provideSignupApi(retrofit: Retrofit): SignupApi {
        return retrofit.create(SignupApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeProfileApi(retrofit: Retrofit): ProfileInfoGetApi {
        return retrofit.create(ProfileInfoGetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommunityApi(retrofit: Retrofit): CommunityApi {
        return retrofit.create(CommunityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIntakeGetApi(retrofit: Retrofit): IntakeGetApi {
        return retrofit.create(IntakeGetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMypageApi(retrofit: Retrofit): MypageApi {
        return retrofit.create(MypageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLifeCheckApi(retrofit: Retrofit): LifeCheckApi {
        return retrofit.create(LifeCheckApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAlarmApi(retrofit: Retrofit): AlarmApi {
        return retrofit.create(AlarmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeApi(retrofit: Retrofit): RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }
}