package com.example.buituananh.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale

object ImageUtils {

    fun getEmbeddedPicture(context: Context, uri: Uri): Uri? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val art = retriever.embeddedPicture
            if (art != null) {
                val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
                file.writeBytes(art)
                Uri.fromFile(file) 
            } else {
                Log.d("AlbumArt", "No embedded image found")
                null
            }
        } catch (e: Exception) {
            Log.e("AlbumArt", "Error: ${e.message}")
            null
        } finally {
            retriever.release()
        }
    }

    fun resizeBitmap(context: Context, bitmap: Bitmap?, reqWidth: Int = 100, reqHeight: Int = 100): Bitmap? {
        if(bitmap == null) return null
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        val scaleWidth = reqWidth.toFloat() / originalWidth
        val scaleHeight = reqHeight.toFloat() / originalHeight
        val scale = minOf(scaleWidth, scaleHeight)

        val newWidth = (originalWidth * scale).toInt()
        val newHeight = (originalHeight * scale).toInt()

        return bitmap.scale(newWidth, newHeight)
    }

    fun resizeImage(context: Context, uri: Uri, reqWidth: Int = 300, reqHeight: Int = 300): Uri? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        val decodedBitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        } ?: return null

        val finalBitmap = decodedBitmap.scale(reqWidth, reqHeight)
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
