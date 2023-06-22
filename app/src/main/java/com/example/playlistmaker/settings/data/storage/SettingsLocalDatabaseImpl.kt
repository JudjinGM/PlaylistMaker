package com.example.playlistmaker.settings.data.storage

import android.content.SharedPreferences

class SettingsLocalDatabaseImpl(private val sharedPreferencesSettings: SharedPreferences) :
    SettingsLocalDatabase {
    override fun setPreferenceBoolean(key: String, value: Boolean) =
        sharedPreferencesSettings.edit().putBoolean(key, value).apply()

    override fun getPreferenceBoolean(key: String, default: Boolean): Boolean =
        sharedPreferencesSettings.getBoolean(key, default)

    companion object {
        const val SETTINGS_PREFS = "settings_prefs"
    }
}