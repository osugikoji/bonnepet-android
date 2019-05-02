package br.com.bonnepet.util.component

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.bonnepet.R

class CircularProgressBar(val context: Context) : CircularProgressDrawable(context) {

    companion object {
        const val STROKE_WIDTH = 10f
        const val CENTER_RADIUS = 60f
    }

    init {
        strokeWidth = STROKE_WIDTH
        centerRadius = CENTER_RADIUS
        setColorSchemeColors((ContextCompat.getColor(context, R.color.color_primary)))
    }
}