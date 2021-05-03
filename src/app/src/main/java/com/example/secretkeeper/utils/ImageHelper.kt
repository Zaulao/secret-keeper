package com.example.secretkeeper.utils

import android.os.Environment
import com.example.secretkeeper.MainApplication.Companion.applicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageHelper {

    object ImageHelper {

        fun getImageFile(): File {
            val imagePath = File(applicationContext().filesDir, "images")
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            return File(imagePath, "JPEG_${timeStamp}_.jpg")
        }

        fun getExternalImageFile(): File {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = applicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            )
        }
    }
}