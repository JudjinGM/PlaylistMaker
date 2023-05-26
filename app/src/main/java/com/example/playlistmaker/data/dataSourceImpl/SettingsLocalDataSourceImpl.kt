package com.example.playlistmaker.data.dataSourceImpl

import com.example.playlistmaker.data.dataSources.SettingsLocalDataSource
import com.example.playlistmaker.data.storage.SettingsLocalDatabase

class SettingsLocalDataSourceImpl(private val database: SettingsLocalDatabase): SettingsLocalDataSource {

    override fun setSetting(key: String, value: Boolean) {
        database.setPreferenceBoolean(key,value)
    }

    override fun getSetting(key: String, defaultValue: Boolean): Boolean {
        return database.getPreferenceBoolean(key,defaultValue)
    }
}