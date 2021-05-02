package com.example.secretkeeper.data

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val SHARED_PREFS = "sharedPrefs"
    private const val KEY = "password"

    fun savePassword(context: Context, password: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY, password)
        editor.apply()
    }

    fun loadPassword(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY, "")
    }
}