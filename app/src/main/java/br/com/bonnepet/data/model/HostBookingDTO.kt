package br.com.bonnepet.data.model

import java.io.Serializable

data class HostBookingDTO(
    var pictureURL: String,
    var user: UserDTO,
    var status: String
) : Serializable
