package com.example.playlistmaker.settings.data.data_source_impl

import com.example.playlistmaker.settings.data.data_source.SettingsLocalDataSource
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabase

class SettingsLocalDataSourceImpl(private val database: SettingsLocalDatabase) :
    SettingsLocalDataSource {

    override fun setSetting(key: String, value: Boolean) {
        database.setPreferenceBoolean(key, value)
    }

    override fun getSetting(key: String, defaultValue: Boolean): Boolean {
        return database.getPreferenceBoolean(key, defaultValue)
    }
}