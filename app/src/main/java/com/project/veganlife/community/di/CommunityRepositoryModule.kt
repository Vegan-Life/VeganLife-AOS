package com.project.veganlife.community.di

import com.project.veganlife.community.data.repositoryimpl.CommunityRepositoryImpl
import com.project.veganlife.community.domain.repository.CommunityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommunityRepositoryModule {
    @Provides
    @Singleton
    fun provideCommunityRepository(communityRepositoryImpl: CommunityRepositoryImpl): CommunityRepository {
        return communityRepositoryImpl
    }
}
