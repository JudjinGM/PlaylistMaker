package com.example.playlistmaker.settings.data.data_source

interface SettingsLocalDataSource {
    fun setSetting(key: String, value: Boolean)
    fun getSetting(key: String, defaultValue: Boolean):Boolean
}