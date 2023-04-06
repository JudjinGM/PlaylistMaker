package com.example.playlistmaker.data.repositorie

import com.example.playlistmaker.data.local.database.SharedPreferencesDatabase

class SharedPreferencesRepository(private val database: SharedPreferencesDatabase) {
    fun saveBoolean(key: String, value : Boolean){
        database.saveBoolean(key, value)
    }
    fun loadBoolean(key: String, defValue: Boolean): Boolean{
        return database.loadBoolean(key, defValue)
    }
}
