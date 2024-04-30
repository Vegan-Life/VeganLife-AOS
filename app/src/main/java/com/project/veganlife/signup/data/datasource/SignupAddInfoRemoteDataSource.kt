package com.project.veganlife.signup.data.datasource

import com.project.veganlife.signup.data.model.SignupRequest

interface SignupAddInfoRemoteDataSource {
    suspend fun setSignup(
        signupRequest: SignupRequest,
    ): String?
}