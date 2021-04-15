package com.example.secretkeeper

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SecureData(@PrimaryKey(autoGenerate = true) val id:Int, val name: String, val format: String, val data: ByteArray, val dateCreated: String) {}