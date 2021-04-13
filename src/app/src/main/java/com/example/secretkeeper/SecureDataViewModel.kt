package com.example.secretkeeper

import androidx.lifecycle.ViewModel

class SecureDataViewModel (
        private val secureDataDao: SecureDataDao
): ViewModel() {

    fun get(): String {
        return "isOk"
    }
}