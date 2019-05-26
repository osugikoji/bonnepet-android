package br.com.bonnepet.data.model

import java.io.Serializable

data class BookingDetailsDTO(
    var stayInitialDate: String,
    var stayFinalDate: String,
    var petDTO: List<PetDTO>,
    var totalPrice: String,
    var status: String,
    var stayDays: String,
    var id: String
) : Serializable
