package br.com.bonnepet.data.enums

import br.com.bonnepet.R

enum class PetBehaviourEnum(val description: Int) {
    CONFIDENT(R.string.confident),
    SHY(R.string.shy),
    AGGRESSIVE(R.string.aggressive),
    SOCIABLE(R.string.sociable),
    INDEPENDENT(R.string.independent);

    companion object {
        fun getDescription(enumName: String): Int? {
            values().forEach { value ->
                if (value.name == enumName) return value.description
            }
            return null
        }
    }
}