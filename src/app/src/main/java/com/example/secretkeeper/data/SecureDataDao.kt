package com.example.secretkeeper.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SecureDataDao {
    @Query("SELECT * FROM SecureData ORDER BY dateCreated DESC")
    fun allSecureData(): LiveData<List<SecureData>>

    @Insert
    fun insert(data: SecureData)

    @Delete
    fun delete(data: SecureData)
}