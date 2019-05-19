package br.com.bonnepet.data.model

import java.io.Serializable

data class HostDTO(
    var id: String,
    var profileDTO: ProfileDTO,
    var petDTO: List<PetDTO>,
    var price: String,
    var sizePreferenceList: List<String>,
    var about: String,
    var bookingDetailsDTO: BookingDetailsDTO?
) : Serializable
