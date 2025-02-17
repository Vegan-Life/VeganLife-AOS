package com.project.veganlife.lifecheck.di

import com.project.veganlife.lifecheck.data.ropositoryImpl.LifeCheckRepositoryImpl
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LifeCheckModule {

    @Provides
    @Singleton
    fun provideLifeCheckRepository(
        lifeCheckRepositoryImpl: LifeCheckRepositoryImpl
    ): LifeCheckRepository {
        return lifeCheckRepositoryImpl
    }
}