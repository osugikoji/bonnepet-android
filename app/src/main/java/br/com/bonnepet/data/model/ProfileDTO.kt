package br.com.bonnepet.data.model

import java.io.Serializable

class ProfileDTO(
    val profileImageURL: String,
    val email: String,
    val userName: String,
    val birthDate: String,
    val cellphone: String,
    val telephone: String,
    val addressDTO: AddressDTO,
    val id: String,
    val editHostDTO: EditHostDTO
) : Serializable