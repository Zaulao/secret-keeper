package com.example.secretkeeper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nome = intent.getStringExtra("nome")
        val login = intent.getStringExtra("login")

        setContentView(R.layout.file_row)
    }
}