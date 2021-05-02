package com.example.secretkeeper

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import com.example.secretkeeper.MainApplication.Companion.applicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class ImageHelper {

    object ImageHelper {
        fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
            val wrapper = ContextWrapper(applicationContext())
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                val stream: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()

                stream.close()
            } catch (e: IOException){
                e.printStackTrace()
            }

            return Uri.parse(file.absolutePath)
        }

        fun rotateImage(img: Bitmap, degree: Float): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(degree)
            val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
            img.recycle()
            return rotatedImg
        }
    }
}