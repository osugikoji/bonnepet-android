package br.com.bonnepet.util.component

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import java.util.*
import kotlin.collections.ArrayList

class CheckBoxDialog(
    private val context: Context,
    private val items: Array<String>,
    private val editText: EditText
) {

    private var checkboxState: BooleanArray =
        ArrayList<Boolean>(Collections.nCopies(items.size, false)).toBooleanArray()

    init {
        buildDialog()
    }

    private fun buildDialog() {
        val currentText = editText.text.split(", ").toTypedArray()

        items.forEachIndexed { index, value ->
            if (currentText.any { it == value }) checkboxState[index] = true
        }

        AlertDialog.Builder(context).apply {
            setMultiChoiceItems(items, checkboxState) { _, which, isChecked ->
                checkboxState[which] = isChecked
            }
            setPositiveButton("OK") { _, _ ->
                var allergiesText = ""
                for (i in 0 until items.size) {
                    val checked = checkboxState[i]
                    if (checked) {
                        allergiesText += items[i] + ", "
                    }
                }
                allergiesText = allergiesText.removeSuffix(", ")
                editText.setText(allergiesText)
            }
        }.show()
    }
}