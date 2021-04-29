package com.example.secretkeeper

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.secretkeeper.databinding.ActivityPhotoBinding


class PhotoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(IMAGE_PATH))
        val bitmap = BitmapFactory.decodeFile(imageUri.toString())

        binding.imageView.setImageBitmap(bitmap)
    }
}