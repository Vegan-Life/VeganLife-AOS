package com.project.veganlife.home.domain.repository

interface HomeProfilePhotoGetRepository {
    suspend fun getProfilePhoto(
    ): String?
}