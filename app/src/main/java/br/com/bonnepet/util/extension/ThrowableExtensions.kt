package br.com.bonnepet.util.extension

import br.com.bonnepet.App
import br.com.bonnepet.R
import com.google.gson.JsonParser
import retrofit2.HttpException

private val app = App()

fun Throwable.error(): String {
    var message = app.getString(R.string.generic_request_error)

    if (this is HttpException) {
        val errorJsonString = this.response().errorBody()?.string()
        message = JsonParser().parse(errorJsonString).asJsonObject["message"].asString
    }
    return message
}