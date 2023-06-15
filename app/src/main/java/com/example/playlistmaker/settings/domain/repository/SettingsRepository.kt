package com.example.playlistmaker.settings.domain.repository

interface SettingsRepository {
    fun setSettingBoolean(key: String, value: Boolean)
    fun getSettingBoolean(key: String, default: Boolean): Boolean
}