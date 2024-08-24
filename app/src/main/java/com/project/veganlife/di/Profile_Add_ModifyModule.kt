package com.project.veganlife.di

import com.project.veganlife.data.datasource.Profile_Add_ModifyDataSource
import com.project.veganlife.data.datasource.Profile_Add_ModifyDataSourceImpl
import com.project.veganlife.data.repositoryImpl.Profile_Add_ModifyRepositoryImpl
import com.project.veganlife.domain.repository.Profile_Add_ModifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Profile_Add_ModifyModule {

    @Provides
    @Singleton
    fun providesProfileAdd_ModifyRepository(
        profileAddModifyrepositoryimpl: Profile_Add_ModifyRepositoryImpl
    ): Profile_Add_ModifyRepository {
        return profileAddModifyrepositoryimpl
    }

    @Provides
    @Singleton
    fun providesProfileAdd_ModifyDataSource(
        profileAddModifydatasourceimpl: Profile_Add_ModifyDataSourceImpl
    ): Profile_Add_ModifyDataSource {
        return profileAddModifydatasourceimpl
    }
}