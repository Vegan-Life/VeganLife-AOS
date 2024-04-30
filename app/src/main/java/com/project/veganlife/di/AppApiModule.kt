package com.project.veganlife.di

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
}