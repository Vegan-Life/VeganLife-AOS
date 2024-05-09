package com.project.veganlife.home.data.datasource

interface HomeProfilePhotoGetDataSource {
    suspend fun getProfilePhoto(
    ): String?
}