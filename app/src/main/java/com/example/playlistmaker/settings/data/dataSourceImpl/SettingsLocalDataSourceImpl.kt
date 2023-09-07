package com.example.playlistmaker.settings.data.dataSourceImpl

import com.example.playlistmaker.settings.data.dataSource.SettingsLocalDataSource
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabase

class SettingsLocalDataSourceImpl(
    private val database: SettingsLocalDatabase
) : SettingsLocalDataSource {

    override fun setSetting(key: String, value: Boolean) {
        database.setPreferenceBoolean(key, value)
    }

    override fun getSetting(key: String, defaultValue: Boolean): Boolean {
        return database.getPreferenceBoolean(key, defaultValue)
    }
}