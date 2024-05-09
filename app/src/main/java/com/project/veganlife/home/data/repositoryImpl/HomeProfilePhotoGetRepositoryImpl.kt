package com.project.veganlife.home.data.repositoryImpl

import com.project.veganlife.home.data.datasource.HomeProfilePhotoGetDataSource
import com.project.veganlife.home.domain.repository.HomeProfilePhotoGetRepository
import javax.inject.Inject

class HomeProfilePhotoGetRepositoryImpl @Inject constructor(
    private val homeProfilePhotoGetDataSource: HomeProfilePhotoGetDataSource
): HomeProfilePhotoGetRepository{
    override suspend fun getProfilePhoto(): String? {
        return homeProfilePhotoGetDataSource.getProfilePhoto()
    }
}