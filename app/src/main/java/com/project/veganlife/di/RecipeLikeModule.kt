package com.project.veganlife.di

import com.project.veganlife.data.datasource.RecipeLikeDataSource
import com.project.veganlife.data.datasource.RecipeLikeDataSourceImpl
import com.project.veganlife.data.repositoryImpl.RecipeLikeRepositoryImpl
import com.project.veganlife.domain.repository.RecipeLikeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeLikeModule {

    @Provides
    @Singleton
    fun providesRecipeLikeRepository(
        recipeLikeRepositoryImpl: RecipeLikeRepositoryImpl,
    ): RecipeLikeRepository {
        return recipeLikeRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesRecipeLikeDataSource(
        recipeLikeDataSourceImpl: RecipeLikeDataSourceImpl,
    ): RecipeLikeDataSource {
        return recipeLikeDataSourceImpl
    }
}