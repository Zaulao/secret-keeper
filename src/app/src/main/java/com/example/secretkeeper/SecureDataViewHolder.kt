package com.example.secretkeeper

import androidx.recyclerview.widget.RecyclerView
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.FileRowBinding

class SecureDataViewHolder(private val binding: FileRowBinding) :
        RecyclerView.ViewHolder(binding.root)
{
    var secureData: SecureData? = null

    fun bindTo(data : SecureData) {
        secureData = data

        binding.name.text = data.name
    }
}