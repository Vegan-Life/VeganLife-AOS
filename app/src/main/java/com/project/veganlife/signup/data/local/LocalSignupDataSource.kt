package com.project.veganlife.signup.data.local

import com.project.veganlife.signup.data.model.SignupVeganType


interface LocalSignupDataSource {
    suspend fun saveVeganTypeList (): List<SignupVeganType>
}