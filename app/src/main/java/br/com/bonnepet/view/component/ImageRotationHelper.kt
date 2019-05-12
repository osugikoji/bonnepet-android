package br.com.bonnepet.view.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImageRotationHelper {

    fun getCorrectImageRotation(imageUrl: String?): File? {
        var loadedBitmap = BitmapFactory.decodeFile(imageUrl)
        var pictureFile: File? = null

        var exif: ExifInterface? = null
        try {
            pictureFile = File(imageUrl)
            exif = ExifInterface(pictureFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var orientation = ExifInterface.ORIENTATION_NORMAL

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> loadedBitmap = rotateBitmap(loadedBitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> loadedBitmap = rotateBitmap(loadedBitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> loadedBitmap = rotateBitmap(loadedBitmap, 270)
        }
        exif?.saveAttributes()
        val os = BufferedOutputStream(FileOutputStream(pictureFile))
        loadedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()


        return pictureFile
    }


    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}