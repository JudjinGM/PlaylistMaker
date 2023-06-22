package com.example.playlistmaker.settings.data.storage

import android.content.Context

class SettingsLocalDatabaseImpl(context: Context) : SettingsLocalDatabase {

    private val settingsSharedPreferencesSettings = context.getSharedPreferences(
        SETTINGS_PREFS, Context.MODE_PRIVATE
    )

    override fun setPreferenceBoolean(key: String, value: Boolean) =
        settingsSharedPreferencesSettings.edit().putBoolean(key, value).apply()

    override fun getPreferenceBoolean(key: String, default: Boolean): Boolean =
        settingsSharedPreferencesSettings.getBoolean(key, default)

    companion object {
        const val SETTINGS_PREFS = "settings_prefs"
    }
}