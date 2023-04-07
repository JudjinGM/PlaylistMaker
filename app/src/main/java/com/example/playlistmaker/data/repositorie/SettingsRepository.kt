package com.example.playlistmaker.data.repositorie

import com.example.playlistmaker.data.local.database.SharedPreferencesDatabase

class SettingsRepository(private val database: SharedPreferencesDatabase):
    BooleanRepository {
    override fun saveBoolean(key: String, value : Boolean){
        database.saveBoolean(key, value)
    }
    override fun loadBoolean(key: String, default: Boolean): Boolean{
        return database.loadBoolean(key, default)
    }
    override fun remove(key: String) {
        database.remove(key)
    }
}
