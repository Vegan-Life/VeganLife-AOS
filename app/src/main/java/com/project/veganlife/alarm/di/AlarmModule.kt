package com.project.veganlife.alarm.di

import com.project.veganlife.alarm.data.repositoryImpl.AlarmRepositoryImpl
import com.project.veganlife.alarm.domain.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {
    @Provides
    @Singleton
    fun provideAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl,
    ): AlarmRepository {
        return alarmRepositoryImpl
    }
}