package br.com.bonnepet.data.model

import java.io.Serializable

class EditProfileDTO(
    val userName: String,
    val birthDate: String,
    val cellphone: String,
    val telephone: String,
    val addressDTO: AddressDTO
): Serializable