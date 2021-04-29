package com.example.secretkeeper

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.ContextCompat
import com.example.secretkeeper.MainApplication.Companion.applicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class ImageHelper {

    object ImageHelper {
        fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
            // Get the context wrapper instance
            val wrapper = ContextWrapper(applicationContext())

            // Initializing a new file
            // The bellow line return a directory in internal storage
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)


            // Create a file to save the image
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                // Get the file output stream
                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()
            } catch (e: IOException){ // Catch the exception
                e.printStackTrace()
            }

            // Return the saved image uri
            return Uri.parse(file.absolutePath)
        }
    }
}