package br.com.bonnepet.util.extension

import android.content.Context
import android.util.Base64
import br.com.bonnepet.R
import org.joda.time.DateTime
import org.joda.time.Months
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject

fun String.getTokenData(): JSONObject =
    JSONObject(
        String(
            Base64.decode(this.split("\\.".toRegex())[1], Base64.URL_SAFE),
            Charsets.UTF_8
        )
    )

fun String.parseToDate(): DateTime? {
    return try {
        DateTime.parse(this, DateTimeFormat.forPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        null
    }
}

fun String.formatToPetAge(context: Context): String? {
    return try {
        val monthsDifference = Months.monthsBetween(this.parseToDate(), DateTime()).months

        val yearNumber: Int = monthsDifference / 12
        val monthNumber: Int = monthsDifference - yearNumber * 12

        var yearText = ""
        var monthText = ""
        var andText = ""

        if (yearNumber == 1) yearText = "$yearNumber ${context.getString(R.string.year)}"
        else if (yearNumber > 1) yearText = "$yearNumber ${context.getString(R.string.years)}"

        if (monthNumber == 1) monthText = "$monthNumber ${context.getString(R.string.month)}"
        else if (monthNumber > 1) monthText = "$monthNumber ${context.getString(R.string.months)}"

        if (monthsDifference > 12) andText = context.getString(R.string.and)

        val petAge = "$yearText $andText $monthText"
        petAge.trim().replace(" +", " ")
    } catch (e: Exception) {
        null
    }
}

fun String.formatToCurrency(): String = "R$ $this"

