package com.example.secretkeeper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SecureData(@PrimaryKey(autoGenerate = true) val id:Int, val name: String, val format: String, val data: ByteArray, val dateCreated: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecureData

        if (id != other.id) return false
        if (name != other.name) return false
        if (format != other.format) return false
        if (!data.contentEquals(other.data)) return false
        if (dateCreated != other.dateCreated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + dateCreated.hashCode()
        return result
    }
}