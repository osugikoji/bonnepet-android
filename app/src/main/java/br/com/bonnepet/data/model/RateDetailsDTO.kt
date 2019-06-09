package br.com.bonnepet.data.model

import java.io.Serializable

data class RateDetailsDTO(
    var bookingId: String,
    var rate: String,
    var feedback: String,
    var userName: String,
    var userImage: String,
    var initialDate: String,
    var finalDate: String
) : Serializable
