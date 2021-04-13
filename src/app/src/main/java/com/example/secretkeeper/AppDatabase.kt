package com.example.secretkeeper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SecureData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun secureDataDao(): SecureDataDao

    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun get(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "database.db").build()
            }
            return instance!!
        }
    }
}