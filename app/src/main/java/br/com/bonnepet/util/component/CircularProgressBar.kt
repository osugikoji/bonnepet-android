package br.com.bonnepet.util.component

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.bonnepet.R

class CircularProgressBar(val activity: Activity) : CircularProgressDrawable(activity) {

    companion object {
        const val STROKE_WIDTH = 10f
        const val CENTER_RADIUS = 60f
    }

    init {
        strokeWidth = STROKE_WIDTH
        centerRadius = CENTER_RADIUS
        setColorSchemeColors((ContextCompat.getColor(activity, R.color.color_primary)))
    }
}