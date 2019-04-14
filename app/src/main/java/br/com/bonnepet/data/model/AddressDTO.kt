package br.com.bonnepet.data.model

import java.io.Serializable

class AddressDTO(
    var cep: String,
    var street: String,
    var number: String,
    var district: String,
    var city: String,
    var state: String
) : Serializable