package com.example.secretkeeper.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SecureDataDao {
    @Query("SELECT * FROM SecureData ORDER BY dateCreated DESC")
    fun allSecureData(): LiveData<List<SecureData>>

    @Insert
    fun insert(data: SecureData)

    @Delete
    fun delete(data: SecureData)

    @Update
    fun update(data: SecureData)
}