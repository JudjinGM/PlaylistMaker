package com.example.playlistmaker.data.storage

import android.content.Context

class SettingsLocalDatabase private constructor(context: Context) {

    private val settingsSharedPreferencesSettings = context.getSharedPreferences(
        SETTINGS_PREFS, Context.MODE_PRIVATE
    )

    fun setPreferenceBoolean(key: String, value: Boolean) =
        settingsSharedPreferencesSettings.edit().putBoolean(key, value).apply()

    fun getPreferenceBoolean(key: String, default: Boolean): Boolean =
        settingsSharedPreferencesSettings.getBoolean(key, default)

    companion object {

        private lateinit var instance: SettingsLocalDatabase
        fun getInstance(context: Context): SettingsLocalDatabase {
            if (!Companion::instance.isInitialized)
                instance = SettingsLocalDatabase(context)
            return instance
        }

        const val SETTINGS_PREFS = "settings_prefs"
        const val APP_THEME_STATUS = "app_theme_status"
    }
}