package com.example.playlistmaker.settings.data.storage

interface SettingsLocalDatabase {
    fun setPreferenceBoolean(key: String, value: Boolean)
    fun getPreferenceBoolean(key: String, default: Boolean): Boolean
}