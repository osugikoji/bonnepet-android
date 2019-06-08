package br.com.bonnepet.util.extension

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.bonnepet.R

fun Activity.progressDialog(): AlertDialog {
    val view = View.inflate(this, R.layout.progress_dialog, null)
    return AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar).apply {
        setView(view)
        setCancelable(false)
    }.create()
}

fun Fragment.progressDialog(): AlertDialog {
    val view = View.inflate(this.context, R.layout.progress_dialog, null)
    return AlertDialog.Builder(this.context!!, android.R.style.Theme_Material_Light_Dialog_NoActionBar).apply {
        setView(view)
        setCancelable(false)
    }.create()
}