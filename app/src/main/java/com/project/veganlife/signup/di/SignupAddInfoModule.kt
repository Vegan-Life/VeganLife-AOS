package com.project.veganlife.signup.di

import com.project.veganlife.signup.data.datasource.SignupAddInfoDataSource
import com.project.veganlife.signup.data.datasource.SignupAddInfoDataSourceImpl
import com.project.veganlife.signup.data.repositoryImpl.SignupAddInfoRepositoryImpl
import com.project.veganlife.signup.domain.SignupAddInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignupAddInfoModule {

    @Provides
    @Singleton
    fun providesSignupAddInfoRepository(
        signupAddInfoRepositoryImpl: SignupAddInfoRepositoryImpl,
    ): SignupAddInfoRepository {
        return signupAddInfoRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideSignupAddInfoDataSource(
        signupAddInfoDataSourceImpl: SignupAddInfoDataSourceImpl,
    ): SignupAddInfoDataSource {
        return signupAddInfoDataSourceImpl
    }
}
