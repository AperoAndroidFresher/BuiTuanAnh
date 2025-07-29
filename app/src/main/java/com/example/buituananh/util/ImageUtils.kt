package com.example.buituananh.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale

object ImageUtils {

    fun resizeImage(context: Context, uri: Uri): Uri? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options, 300, 300)
        options.inJustDecodeBounds = false

        val decodedBitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        } ?: return null

        val finalBitmap = decodedBitmap.scale(300, 300)
        Log.d("A12", "${finalBitmap.byteCount} ${finalBitmap.width} ${finalBitmap.height}")
        val file = File(context.cacheDir, "resized_${System.currentTimeMillis()}.webp")
        FileOutputStream(file).use { out ->
            finalBitmap.compress(Bitmap.CompressFormat.WEBP, 85, out)
        }

        return file.toUri()
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if(height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if(heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

}