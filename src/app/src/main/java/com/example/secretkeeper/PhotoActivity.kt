package com.example.secretkeeper

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.ImageHelper.ImageHelper.rotateImage
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityPhotoBinding
import java.io.File
import java.io.UTFDataFormatException
import java.nio.charset.StandardCharsets
import java.util.*


class PhotoActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var binding : ActivityPhotoBinding
    private lateinit var imageUri : Uri
    private var secureData: SecureData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SecureDataViewModelFactory(application)).get(SecureDataViewModel::class.java)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        secureData = intent.getParcelableExtra(SECURE_DATA)
        imageUri = Uri.parse(intent.getStringExtra(IMAGE_PATH))

        putImageInView()

        binding.takePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.addFile.setOnClickListener {
            secureData?.let {
                viewModel.remove(it)
                secureData = null
            }
            secureData = imageUri.lastPathSegment?.let { lastPathSegment ->
                SecureData(0,
                    lastPathSegment,
                    IMG_FORMAT,
                    imageUri.toString().toByteArray(StandardCharsets.UTF_8),
                    Calendar.getInstance().toString())
            }
            viewModel.insertData(secureData!!)
            finish()
        }
    }

    private fun putImageInView() {
        val ei = ExifInterface(imageUri.toString())
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val bitmap = BitmapFactory.decodeFile(imageUri.toString())
        var rotatedBitmap: Bitmap? = null

        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }

        binding.imageView.setImageBitmap(rotatedBitmap)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            deleteImage()

            imageUri = ImageHelper.ImageHelper.saveImageToInternalStorage(imageBitmap)
            putImageInView()
        }
    }

    private fun deleteImage() {
        secureData?.let {
            viewModel.remove(it)
            secureData = null
        }

        val file = File(imageUri.path)
        file.delete()
        if (file.exists()) {
            file.canonicalFile.delete()
            if (file.exists()) {
                applicationContext.deleteFile(file.name)
            }
        }
    }
}