package br.com.bonnepet.util.extension

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun Uri.imageUrl(context: Context?): String? {
    val imageUrl: String?
    val selectedImage = this
    val filePath = arrayOf(MediaStore.Images.Media.DATA)
    val c = context?.contentResolver?.query(selectedImage, filePath, null, null, null)
    c!!.moveToFirst()
    val columnIndex = c.getColumnIndex(filePath[0])
    imageUrl = c.getString(columnIndex)
    c.close()
    return  imageUrl
}


