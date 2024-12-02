package com.project.veganlife.signup.di

import com.project.veganlife.signup.data.datasource.SignupAddInfoRemoteDataSourceImpl
import com.project.veganlife.signup.data.repositoryImpl.SignupRepositoryImpl
import com.project.veganlife.signup.domain.repository.SignupRepository
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
        signupRepositoryImpl: SignupRepositoryImpl
    ): SignupRepository {
        return signupRepositoryImpl
    }
}
