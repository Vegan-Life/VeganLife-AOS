package com.project.veganlife.signup.di

import com.project.veganlife.signup.data.local.LocalSignupDataSource
import com.project.veganlife.signup.data.local.LocalSignupDataSourceImpl
import com.project.veganlife.signup.data.repositoryImpl.SignupVeganTypeRepositoryImpl
import com.project.veganlife.signup.domain.SignupVeganTypeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SignupVeganTypeModule {
    @Provides
    @Singleton
    fun provideSignupVeganTypeRepository(
        localSignupDataSource: LocalSignupDataSource
    ): SignupVeganTypeRepository {
        return SignupVeganTypeRepositoryImpl(localSignupDataSource)
    }

    @Provides
    @Singleton
    fun provideLocalSignupDataSource(): LocalSignupDataSource {
        return LocalSignupDataSourceImpl()
    }
}