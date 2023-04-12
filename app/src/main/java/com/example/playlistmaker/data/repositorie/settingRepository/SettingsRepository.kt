package com.example.playlistmaker.data.repositorie.settingRepository

import com.example.playlistmaker.data.local.database.LocalDatabase

class SettingsRepository(private val database: LocalDatabase) {
    fun saveBooleanSetting(key: String, value: Boolean) {
        database.saveBoolean(key, value)
    }

    fun loadBooleanSetting(key: String, default: Boolean): Boolean {
        return database.loadBoolean(key, default)
    }

}
