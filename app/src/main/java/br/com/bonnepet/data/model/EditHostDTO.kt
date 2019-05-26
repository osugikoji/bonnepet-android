package br.com.bonnepet.data.model

import java.io.Serializable

data class EditHostDTO(
    val id: String,
    var price: String,
    var sizeList: List<String>,
    var about: String
) : Serializable