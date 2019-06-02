package br.com.bonnepet.data.enums

import br.com.bonnepet.R

enum class PetSizeEnum(val description: Int) {
    SMALL(R.string.small),
    MEDIUM(R.string.medium),
    LARGE(R.string.large);

    companion object {
        fun getDescription(enumName: String): Int? {
            values().forEach { value ->
                if (value.name == enumName) return value.description
            }
            return null
        }
    }
}