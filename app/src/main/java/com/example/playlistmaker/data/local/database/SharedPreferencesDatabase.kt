package com.example.playlistmaker.data.local.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.PLAYLIST_PREFS

class SharedPreferencesDatabase private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun loadBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun loadString(key: String, default: String){
        sharedPreferences.getString(key, default)
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    companion object {

        private lateinit var instance: SharedPreferencesDatabase

        fun getInstance(context: Context): SharedPreferencesDatabase {
            if (!::instance.isInitialized)
                instance = SharedPreferencesDatabase(context)
            return instance
        }
    }
}