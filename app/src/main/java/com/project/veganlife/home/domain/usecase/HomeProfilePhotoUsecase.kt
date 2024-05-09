package com.project.veganlife.home.domain.usecase

import com.project.veganlife.home.domain.repository.HomeProfilePhotoGetRepository
import javax.inject.Inject

class HomeProfilePhotoUsecase @Inject constructor(
    val homeProfilePhotoGetRepository: HomeProfilePhotoGetRepository,
) {
    suspend operator fun invoke(): String? {
        return homeProfilePhotoGetRepository.getProfilePhoto()
    }
}