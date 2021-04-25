package com.example.secretkeeper

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.FileRowBinding

class SecureDataViewHolder(private val binding: FileRowBinding) :
        RecyclerView.ViewHolder(binding.root)
{
    var secureData: SecureData? = null

    init {
        binding.root.setOnClickListener {
            val rootContext = binding.root.context
            val intent = Intent(rootContext, NoteActivity::class.java)
            intent.putExtra("secure_data", secureData)
            rootContext.startActivity(intent)
        }
    }

    fun bindTo(data : SecureData) {
        secureData = data

        binding.name.text = data.name
    }
}