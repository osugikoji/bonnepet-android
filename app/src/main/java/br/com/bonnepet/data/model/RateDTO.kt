package br.com.bonnepet.data.model

import java.io.Serializable

data class RateDTO(
    var bookingId: String,
    var rate: String,
    var feedback: String
) : Serializable
