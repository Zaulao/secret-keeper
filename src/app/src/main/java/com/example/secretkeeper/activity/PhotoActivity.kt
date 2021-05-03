package com.example.secretkeeper.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityPhotoBinding
import com.example.secretkeeper.securedata.SecureDataViewModel
import com.example.secretkeeper.securedata.SecureDataViewModelFactory
import com.example.secretkeeper.utils.*
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*


class PhotoActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var binding : ActivityPhotoBinding
    private lateinit var imageUri : Uri
    private var secureData: SecureData? = null
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SecureDataViewModelFactory(application)).get(SecureDataViewModel::class.java)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        secureData = intent.getParcelableExtra(SECURE_DATA)
        imageUri = Uri.parse(intent.getStringExtra(IMAGE_PATH))

        binding.imageView.setImageURI(imageUri)

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

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    ImageHelper.ImageHelper.getExternalImageFile().apply { currentPhotoPath = absolutePath }
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            FILE_PROVIDER,
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            deleteImage()
            var imageFile = ImageHelper.ImageHelper.getImageFile()
            val externalFile = File(currentPhotoPath)
            externalFile.copyTo(imageFile)
            externalFile.delete()

            imageUri = Uri.fromFile(imageFile)
            binding.imageView.setImageURI(imageUri)
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