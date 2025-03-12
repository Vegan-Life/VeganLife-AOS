package com.project.veganlife.utils.ui

import com.project.veganlife.R

class VeganTypeChange {
    companion object {
        fun changeVeganType(type: String): String {
            return when (type) {
                "VEGAN" -> "비건"
                "OVO" -> "오보"
                "LACTO" -> "락토"
                "PESCO" -> "페스코"
                else -> "락토오보"
            }
        }

        fun changeBackground(type: String): Int {
            return when(type) {
                "VEGAN" -> R.drawable.all_vegan_type_vegan_background
                "LACTO" -> R.drawable.all_vegan_type_lacto_background
                "OVO" -> R.drawable.all_vegan_type_ovo_background
                "PESCO" -> R.drawable.all_vegan_type_pesco_background
                else -> R.drawable.all_vegan_type_lacto_ovo_background
            }
        }

        fun changeVeganTypeTextColor(type: String): Int {
            return when(type) {
                "VEGAN" -> R.color.base3
                else -> R.color.sub_gray1
            }
        }
    }
}