package br.com.bonnepet.data.model

import java.io.Serializable

data class UserDTO(
    var id: String? = null,
    var email: String,
    var name: String,
    var password: String,
    var birthDate: String,
    var cellphone: String,
    var telephone: String? = null,
    var addressDTO: AddressDTO? = null
) : Serializable


