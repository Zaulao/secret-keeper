package com.example.secretkeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityNoteBinding
import java.nio.charset.Charset
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

        secureData = intent.getParcelableExtra("secure_data")

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
                viewModel.insertData(SecureData(0, binding.title.text.toString(), "txt",binding.note.text.toString().toByteArray(
                    StandardCharsets.UTF_8), Calendar.getInstance().toString()))
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}