package com.example.playlistmaker.data.dataSources

interface SettingsLocalDataSource {
    fun setSetting(key: String, value: Boolean)
    fun getSetting(key: String, defaultValue: Boolean):Boolean
}