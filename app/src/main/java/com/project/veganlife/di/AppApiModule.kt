package com.project.veganlife.di

import com.project.veganlife.data.remote.DailyIntakeGetApi
import com.project.veganlife.data.remote.ProfileInfoGetApi
import com.project.veganlife.data.remote.RecipeLikeApi
import com.project.veganlife.data.remote.RecipeLikeCancelApi
import com.project.veganlife.data.remote.RecommendedIntakeGetApi
import com.project.veganlife.lifecheck.data.remote.LifeCheckMonthlyCalorieGetApi
import com.project.veganlife.lifecheck.data.remote.LifeCheckWeeklyCalorieGetApi
import com.project.veganlife.lifecheck.data.remote.LifeCheckYearlyCalorieGetApi
import com.project.veganlife.mypage.data.remote.MypageGetMyPostedCommentApi
import com.project.veganlife.mypage.data.remote.MypageGetMyPostedFeedApi
import com.project.veganlife.mypage.data.remote.MypageGetScrapedRecipeApi
import com.project.veganlife.mypage.data.remote.MypageWithDrawalApi
import com.project.veganlife.signup.data.remote.SignupApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AppApiModule {
    @Provides
    fun provideSignupApi(retrofit: Retrofit): SignupApi {
        return retrofit.create(SignupApi::class.java)
    }

    @Provides
    fun provideHomeProfileApi(retrofit: Retrofit): ProfileInfoGetApi {
        return retrofit.create(ProfileInfoGetApi::class.java)
    }

    @Provides
    fun provideRecommendedIntakeGetApi(retrofit: Retrofit): RecommendedIntakeGetApi {
        return retrofit.create(RecommendedIntakeGetApi::class.java)
    }

    @Provides
    fun provideDailyIntakeGetApi(retrofit: Retrofit): DailyIntakeGetApi {
        return retrofit.create(DailyIntakeGetApi::class.java)
    }

    @Provides
    fun provideMypageDeleteWithdrawalApi(retrofit: Retrofit): MypageWithDrawalApi {
        return retrofit.create(MypageWithDrawalApi::class.java)
    }

    @Provides
    fun provideLifeCheckWeeklyCalorieGetApi(retrofit: Retrofit): LifeCheckWeeklyCalorieGetApi {
        return retrofit.create(LifeCheckWeeklyCalorieGetApi::class.java)
    }

    @Provides
    fun provideMyPostedFeedApi(retrofit: Retrofit): MypageGetMyPostedFeedApi {
        return retrofit.create(MypageGetMyPostedFeedApi::class.java)
    }

    @Provides
    fun provideMyPostedCommentApi(retrofit: Retrofit): MypageGetMyPostedCommentApi {
        return retrofit.create(MypageGetMyPostedCommentApi::class.java)
    }

    @Provides
    fun provideScrapedRecipeApi(retrofit: Retrofit): MypageGetScrapedRecipeApi {
        return retrofit.create(MypageGetScrapedRecipeApi::class.java)
    }

    @Provides
    fun provideScrapedRecipeLikeApi(retrofit: Retrofit): RecipeLikeApi {
        return retrofit.create(RecipeLikeApi::class.java)
    }

    @Provides
    fun provideScrapedRecipeLikeCancelApi(retrofit: Retrofit): RecipeLikeCancelApi {
        return retrofit.create(RecipeLikeCancelApi::class.java)
    }

    @Provides
    fun provideLifeCheckMonthlyCalorieGetApi(retrofit: Retrofit): LifeCheckMonthlyCalorieGetApi {
        return retrofit.create(LifeCheckMonthlyCalorieGetApi::class.java)
    }

    @Provides
    fun provideLifeCheckYearlyCalorieGetApi(retrofit: Retrofit): LifeCheckYearlyCalorieGetApi {
        return retrofit.create(LifeCheckYearlyCalorieGetApi::class.java)
    }
}