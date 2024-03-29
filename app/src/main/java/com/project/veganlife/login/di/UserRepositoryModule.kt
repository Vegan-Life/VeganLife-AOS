package com.project.veganlife.login.di

import android.content.SharedPreferences
import com.project.veganlife.login.data.local.LocalUserDataSource
import com.project.veganlife.login.data.local.LocalUserDataSourceImpl
import com.project.veganlife.login.data.repositoryImpl.UserRepositoryImpl
import com.project.veganlife.login.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository {
        return userRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideLocalUserDataSource(
        sharedPreferences: SharedPreferences,
    ): LocalUserDataSource {
        return LocalUserDataSourceImpl(sharedPreferences)
    }
}