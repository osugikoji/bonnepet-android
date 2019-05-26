package br.com.bonnepet.data.model

import java.io.Serializable

data class HostBookingDTO(
    var profileDTO: ProfileDTO,
    var bookingDetailsDTO: BookingDetailsDTO
) : Serializable
