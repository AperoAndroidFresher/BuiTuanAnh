package com.example.buituananh.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale

object ImageUtils {

    fun extractAlbumArt(contentResolver: ContentResolver, audioUri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            contentResolver.openFileDescriptor(audioUri, "r")?.use { pfd ->
                retriever.setDataSource(pfd.fileDescriptor)
                val artBytes = retriever.embeddedPicture
                if (artBytes != null) {
                    return BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
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