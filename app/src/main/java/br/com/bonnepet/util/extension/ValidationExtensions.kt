package br.com.bonnepet.util.extension

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.TextTypeEnum

fun String?.isValidDate(): Boolean {
    return !this.isNullOrEmpty() && this.parseToDate() != null
}

fun String?.isNotValidDate(): Boolean {
    return !this.isValidDate()
}

fun String?.isValidEmail(): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.isNotValidEmail(): Boolean =
    !this.isValidEmail()

fun String?.isValidPhone(): Boolean =
    android.util.Patterns.PHONE.matcher(this).matches()

fun String?.isNotValidPhone(): Boolean =
    !this.isValidPhone()


fun EditText.validate(context: Context, textTypeEnum: TextTypeEnum? = null): Boolean {
    val text = this.text.toString()
    if (text.isEmpty()) {
        this.error = "Este campo é obrigatório"
        return false
    }

    when (textTypeEnum) {
        TextTypeEnum.EMAIL -> {
            if (text.isNotValidEmail()) {
                this.error = context.getString(R.string.invalid_email)
                return false
            }
        }
        TextTypeEnum.DATE -> {
            if (text.isNotValidDate()) {
                this.error = context.getString(R.string.invalid_date)
                return false
            }
        }
        TextTypeEnum.PHONE -> {
            if (text.isNotValidPhone()) {
                this.error = context.getString(R.string.invalid_phone)
                return false
            }
        }
        else -> Unit
    }
    return true
}