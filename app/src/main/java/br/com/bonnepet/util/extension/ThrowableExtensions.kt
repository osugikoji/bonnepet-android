package br.com.bonnepet.util.extension

import com.google.gson.JsonParser
import retrofit2.HttpException

fun Throwable.error(): String {
    var message = "Não foi possível executar a requisição"

    if (this is HttpException) {
        val errorJsonString = this.response().errorBody()?.string()
        message = JsonParser().parse(errorJsonString).asJsonObject["message"].asString
    }
    return message
}