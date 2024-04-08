package com.project.veganlife.signup.domain

import com.project.veganlife.signup.data.model.SignupVeganType

interface SignupVeganTypeRepository {
    suspend fun saveVeganTypeList (): List<SignupVeganType>
}