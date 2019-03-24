package br.com.bonnepet.util.extension

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.validate(): Boolean {
    val text = this.text
    if (text.isEmpty()) {
        this.error = "Este campo é obrigatório"
        return false
    }

    when (this.inputType - 1) {
        EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
            if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                this.error = "Por favor, insira um endereço de e-mail válido"
                return false
            }
        }
    }

    return true
}