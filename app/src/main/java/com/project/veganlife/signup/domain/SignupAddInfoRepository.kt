package com.project.veganlife.signup.domain

import com.project.veganlife.signup.data.model.SignupRequest

interface SignupAddInfoRepository {
    suspend fun setSignup(
        signupRequest: SignupRequest,
    ): String?
}