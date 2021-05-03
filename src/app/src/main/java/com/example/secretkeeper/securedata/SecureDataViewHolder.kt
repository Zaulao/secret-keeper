package com.example.secretkeeper.securedata

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.secretkeeper.activity.NoteActivity
import com.example.secretkeeper.activity.PhotoActivity
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.FileRowBinding
import com.example.secretkeeper.utils.IMAGE_PATH
import com.example.secretkeeper.utils.IMG_FORMAT
import com.example.secretkeeper.utils.SECURE_DATA
import com.example.secretkeeper.utils.TEXT_FORMTAT
import java.nio.charset.StandardCharsets

class SecureDataViewHolder(private val binding: FileRowBinding) :
        RecyclerView.ViewHolder(binding.root)
{
    var secureData: SecureData? = null

    init {
        binding.root.setOnClickListener {
            when (secureData?.format) {
                IMG_FORMAT -> {
                    val rootContext = binding.root.context
                    val intent = Intent(rootContext, PhotoActivity::class.java)
                    intent.putExtra(SECURE_DATA, secureData)
                    intent.putExtra(IMAGE_PATH, String(secureData!!.data, StandardCharsets.UTF_8))
                    rootContext.startActivity(intent)
                }
                TEXT_FORMTAT -> {
                    val rootContext = binding.root.context
                    val intent = Intent(rootContext, NoteActivity::class.java)
                    intent.putExtra(SECURE_DATA, secureData)
                    rootContext.startActivity(intent)
                }
            }
        }
    }

    fun bindTo(data : SecureData) {
        secureData = data

        binding.name.text = data.name
        binding.type.text = data.format
    }
}