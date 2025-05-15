package com.project.veganlife.signup.data.model

data class SignupAddInfo(
    val nickname: String = "",
    val gender: String = "", // "M" or "F"
    val vegetarianType: String = "",
    val birthYear: Int? = null,
    val height: Int? = null,
    val weight: Int? = null
) {
    companion object {
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 10
        private val NICKNAME_REGEX = "[가-힣a-zA-Z]+".toRegex()
    }

    fun validate(): Map<Field, String?> {
        return mapOf(
            Field.NICKNAME to when {
                nickname.isEmpty() -> "닉네임을 입력해주세요"
                !nickname.matches(NICKNAME_REGEX) -> "특수문자는 사용할 수 없습니다"
                nickname.length !in MIN_NICKNAME_LENGTH..MAX_NICKNAME_LENGTH -> "2~10자 이내로 입력해주세요"
                else -> null
            },
            Field.GENDER to if (gender.isEmpty()) "성별을 선택해주세요" else null,
            Field.BIRTH_YEAR to when {
                birthYear == null -> "출생연도를 입력해주세요"
                birthYear !in 1900..2999 -> "유효한 연도를 입력해주세요"
                else -> null
            },
            Field.VEGETARIAN_TYPE to when {
                vegetarianType.isEmpty() -> "비건 타입을 선택해주세요"
                else -> null
            },
            Field.HEIGHT to when {
                height == null -> "키를 입력해주세요"
                height !in 1..200 -> "유효한 키를 입력해주세요"
                else -> null
            },
            Field.WEIGHT to when {
                weight == null -> "몸무게를 입력해주세요"
                weight !in 1..200 -> "유효한 몸무게를 입력해주세요"
                else -> null
            }
        ).filterValues { it != null }
    }

    enum class Field { NICKNAME, GENDER, BIRTH_YEAR, HEIGHT, WEIGHT, VEGETARIAN_TYPE }
}