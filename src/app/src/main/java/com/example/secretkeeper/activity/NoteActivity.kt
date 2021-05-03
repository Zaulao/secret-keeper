package com.example.secretkeeper.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityNoteBinding
import com.example.secretkeeper.securedata.SecureDataViewModel
import com.example.secretkeeper.securedata.SecureDataViewModelFactory
import com.example.secretkeeper.utils.SECURE_DATA
import com.example.secretkeeper.utils.TEXT_FORMTAT
import java.nio.charset.StandardCharsets
import java.util.*

class NoteActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var binding: ActivityNoteBinding
    private var secureData: SecureData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SecureDataViewModelFactory(application)).get(SecureDataViewModel::class.java)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        secureData = intent.getParcelableExtra(SECURE_DATA)

        secureData?.let {
            binding.title.setText(it.name)
            binding.note.setText(String(it.data, StandardCharsets.UTF_8))
        }

        binding.saveNote.setOnClickListener {
            if (secureData != null) {
                secureData!!.name = binding.title.text.toString()
                secureData!!.data = binding.note.text.toString().toByteArray()
                secureData!!.dateCreated = Calendar.getInstance().toString()
                viewModel.update(secureData!!)
            } else {
                viewModel.insertData(SecureData(0, binding.title.text.toString(), TEXT_FORMTAT,binding.note.text.toString().toByteArray(
                    StandardCharsets.UTF_8), Calendar.getInstance().toString()))
            }
            finish()
        }
    }
}