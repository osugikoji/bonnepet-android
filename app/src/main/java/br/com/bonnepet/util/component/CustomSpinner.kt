package br.com.bonnepet.util.component

import android.content.Context
import android.widget.ArrayAdapter
import br.com.bonnepet.R

class CustomSpinner(
    context: Context,
    list: ArrayList<String>
) : ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, list)