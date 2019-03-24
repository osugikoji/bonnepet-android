package br.com.bonnepet.util.extension

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun Intent.image(context: Context): Bitmap? {
    val imageUri: Uri? = this.data
    val imageStream = context.contentResolver.openInputStream(imageUri ?: return null)
    return BitmapFactory.decodeStream(imageStream)
}


