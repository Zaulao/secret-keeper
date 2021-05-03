package com.example.secretkeeper.securedata

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.data.AppDatabase

class SecureDataViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        val secureDataDao = AppDatabase.get(app).secureDataDao()
        return SecureDataViewModel(secureDataDao) as T
    }
}