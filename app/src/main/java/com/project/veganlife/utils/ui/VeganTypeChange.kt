package com.project.veganlife.utils.ui

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
    }
}