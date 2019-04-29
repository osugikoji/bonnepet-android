package br.com.bonnepet.data.model

import java.io.Serializable

data class RequestBookingDTO(
    var pictureURL: String,
    var user: UserDTO,
    var status: String
) : Serializable
