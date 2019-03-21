package br.com.lardopet.util.extension

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun Intent.image(context: Context): Bitmap {
    val imageUri = this.data
    val imageStream = context.contentResolver.openInputStream(imageUri)
    return BitmapFactory.decodeStream(imageStream)
}


