package com.example.secretkeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [SecureData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun secureDataDao(): SecureDataDao

    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun get(context: Context): AppDatabase {
            if (instance == null) {
                val builder = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "encrypted")
                val factory = SupportFactory(SQLiteDatabase.getBytes("PassPhrase".toCharArray()))
                builder.openHelperFactory(factory)
                instance = builder.build()
            }
            return instance!!
        }
    }
}