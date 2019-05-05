package br.com.bonnepet.data.model

import java.io.Serializable

data class HostDTO(
    var pictureURL: String,
    var name: String,
    var price: String,
    var addressDTO: AddressDTO
) : Serializable
