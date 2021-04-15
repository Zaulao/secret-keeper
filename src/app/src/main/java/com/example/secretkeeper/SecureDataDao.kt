package com.example.secretkeeper

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
    fun insert(cheese: SecureData)

    @Delete
    fun delete(cheese: SecureData)
}