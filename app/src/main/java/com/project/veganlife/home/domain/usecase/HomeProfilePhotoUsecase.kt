package com.project.veganlife.home.domain.usecase

import com.project.veganlife.home.data.datasource.HomeProfilePhotoGetDataSource
import javax.inject.Inject

class HomeProfilePhotoUsecase @Inject constructor(
    val homeProfilePhotoGetDataSource: HomeProfilePhotoGetDataSource
) {
    suspend operator fun invoke(): String? {
        return homeProfilePhotoGetDataSource.getProfilePhoto()
    }
}