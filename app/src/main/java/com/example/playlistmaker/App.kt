package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SETTING_PREFS = "settings_preferences"
const val APP_THEME_STATUS = "dark_theme_status"


class App : Application() {
    var appTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(SETTING_PREFS, MODE_PRIVATE)
        appTheme = sharedPreferences.getBoolean(APP_THEME_STATUS, false)
        switchTheme(appTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        appTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

