package br.com.bonnepet.data.model

import java.io.Serializable

data class RequestBookingDTO(
    var host: HostDTO,
    var status: String
) : Serializable
