package com.example.secretkeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityNoteBinding
import java.util.*

class NoteActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SecureDataViewModelFactory(application)).get(SecureDataViewModel::class.java)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveNote.setOnClickListener {
            binding.title.text = "Test"
            viewModel.insertData(SecureData(0, binding.title.text.toString(), "txt",binding.note.text.toString().toByteArray(), Calendar.getInstance().toString()))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}