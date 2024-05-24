package com.project.veganlife.data.repositoryImpl

import com.project.veganlife.data.datasource.ProfileGetDataSource
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.repository.ProfileGetRespository
import javax.inject.Inject

class ProfileGetRepositoryImpl @Inject constructor(
    private val profileGetDataSource: ProfileGetDataSource,
) : ProfileGetRespository {
    override suspend fun getInformation(): ApiResult<ProfileResponse> {
        return profileGetDataSource.getInformation()
    }
}