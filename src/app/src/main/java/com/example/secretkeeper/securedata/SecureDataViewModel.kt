package com.example.secretkeeper.securedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.data.SecureDataDao
import com.example.secretkeeper.ioThread

class SecureDataViewModel (
        private val secureDataDao: SecureDataDao
): ViewModel() {

    fun getSecureData(): LiveData<List<SecureData>> {
        return secureDataDao.allSecureData()
    }

    fun insertData(data: SecureData) = ioThread {
        secureDataDao.insert(data)
    }

    fun remove(data: SecureData) = ioThread {
        secureDataDao.delete(data)
    }

    fun update(data: SecureData) = ioThread {
        secureDataDao.update(data)
    }
}