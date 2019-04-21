package br.com.bonnepet.util.extension

import android.app.Application
import br.com.bonnepet.R
import com.google.gson.JsonParser
import retrofit2.HttpException

fun Throwable.error(app: Application): String {
    var message = app.getString(R.string.generic_request_error)

    if (this is HttpException) {
        val errorJsonString = this.response().errorBody()?.string()
        message = JsonParser().parse(errorJsonString).asJsonObject["errorMessage"].asString
    }
    return message
}