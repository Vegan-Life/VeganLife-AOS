package com.project.veganlife.signup.domain

import com.project.veganlife.signup.data.model.SignupVeganType
import javax.inject.Inject

class SignupVeganTypeUsecase @Inject constructor(
    val signupVeganTypeRepository: SignupVeganTypeRepository,
) {
    suspend operator fun invoke(): List<SignupVeganType> {
        return signupVeganTypeRepository.saveVeganTypeList()
    }
}