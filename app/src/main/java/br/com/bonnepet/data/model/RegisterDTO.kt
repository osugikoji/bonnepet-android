package br.com.bonnepet.data.model

data class RegisterDTO(
    var name: String = "",
    var email: String = "",
    var birthDate: String = "",
    var gender: String = "",
    var cellPhone: String = "",
    var telephone: String = "",
    var cep: String = "",
    var district: String = "",
    var street: String = "",
    var number: String = "",
    var state: String = "",
    var city: String = ""
)