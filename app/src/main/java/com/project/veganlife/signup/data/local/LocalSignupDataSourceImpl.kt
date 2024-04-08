package com.project.veganlife.signup.data.local

import com.project.veganlife.R
import com.project.veganlife.signup.data.model.SignupVeganType

class LocalSignupDataSourceImpl : LocalSignupDataSource {
    override suspend fun saveVeganTypeList(): List<SignupVeganType> {
        return listOf(
            SignupVeganType(
                R.drawable.all_vegan_type_vegan,
                "비건",
                "Vegan",
                R.string.signup_vegan_description
            ),
            SignupVeganType(
                R.drawable.all_vegan_type_lacto,
                "락토",
                "Lacto",
                R.string.signup_lacto_description
            ),
            SignupVeganType(
                R.drawable.all_vegan_type_ovo,
                "오보",
                "Ovo",
                R.string.signup_ovo_description
            ),
            SignupVeganType(
                R.drawable.all_vegan_type_lacto_ovo,
                "락토오보",
                "Lacto Ovo",
                R.string.signup_lacto_ovo_description
            ),
            SignupVeganType(
                R.drawable.all_vegan_type_pesco,
                "페스코",
                "Pesco",
                R.string.signup_pesco_description
            ),
            )
    }
}