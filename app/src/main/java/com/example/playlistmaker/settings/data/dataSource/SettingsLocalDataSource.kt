package com.example.playlistmaker.settings.data.dataSource

interface SettingsLocalDataSource {
    fun setSetting(key: String, value: Boolean)
    fun getSetting(key: String, defaultValue: Boolean): Boolean
}