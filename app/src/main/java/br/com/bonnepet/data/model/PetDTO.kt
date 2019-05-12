package br.com.bonnepet.data.model

import java.io.Serializable

data class PetDTO(
    var pictureURL: String?,
    var name: String,
    var breed: String,
    var gender: String,
    var birthDate: String?,
    var size: String,
    var observations: String,
    var behaviours: List<String>,
    var id: String = ""
) : Serializable
