package br.com.bonnepet.util.extension

import android.util.Base64
import org.json.JSONObject

    fun String.getTokenData(): JSONObject =
            JSONObject(String(Base64.decode(this.split("\\.".toRegex())[1], Base64.URL_SAFE),
                    Charsets.UTF_8))

