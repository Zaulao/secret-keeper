package com.example.secretkeeper

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.data.SecureDataDao

class SecureDataViewModel (
        private val secureDataDao: SecureDataDao
): ViewModel() {

    fun getSecureData(): LiveData<List<SecureData>> {
        return secureDataDao.allSecureData()
    }

    fun insertData(data: SecureData) {
        secureDataDao.insert(data)
    }

    fun remove(data: SecureData) {
        secureDataDao.delete(data)
    }
}