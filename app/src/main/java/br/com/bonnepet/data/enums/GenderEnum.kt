package br.com.bonnepet.data.enums

import br.com.bonnepet.R

enum class GenderEnum(val description: Int) {
    MALE(R.string.male),
    FEMALE(R.string.female);

    companion object {
        fun getDescription(name: String): Int? {
            values().forEach {
                if (it.name == name) return it.description
            }
            return null
        }
    }
}