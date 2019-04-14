package br.com.bonnepet.data.model

import com.google.gson.annotations.SerializedName

data class CepDTO(
    var cep: String,
    @SerializedName("logradouro")
    var street: String,
    @SerializedName("complemento")
    var complement: String,
    @SerializedName("bairro")
    var district: String,
    @SerializedName("localidade")
    var city: String,
    @SerializedName("uf")
    var state: String,
    @SerializedName("erro")
    var error: String
)
