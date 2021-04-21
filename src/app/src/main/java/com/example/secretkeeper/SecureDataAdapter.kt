package com.example.secretkeeper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.FileRowBinding

class SecureDataAdapter(
        private val secureDataList:List<SecureData>,
        private val inflater: LayoutInflater) :
        RecyclerView.Adapter<SecureDataViewHolder>()
{
    override fun getItemCount(): Int = secureDataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecureDataViewHolder {
        val binding = FileRowBinding.inflate(inflater, parent, false)
        return SecureDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SecureDataViewHolder, position: Int) {
        holder.bindTo(secureDataList[position])
    }
}