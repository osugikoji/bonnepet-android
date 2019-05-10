package br.com.bonnepet.data.model

import java.io.Serializable

data class HostDTO(
    var pictureURL: String,
    var name: String,
    var aboutMe: String,
    var pet: List<PetDTO>,
    var phone: List<String>,
    var price: String,
    var preferencePetSize: List<String>,
    var addressDTO: AddressDTO
) : Serializable
