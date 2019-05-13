package br.com.bonnepet.data.model

import java.io.Serializable

data class NewBookingDTO(
    var hostId: String,
    var stayInitialDate: String,
    var stayFinalDate: String,
    var petIds: List<String>,
    var totalPrice: String
) : Serializable
