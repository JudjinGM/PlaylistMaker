package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val SETTING_PREFS = "settings_preferences"
const val APP_THEME_STATUS = "dark_theme_status"


class App : Application() {
    var appTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(SETTING_PREFS, MODE_PRIVATE)
        appTheme = sharedPreferences.getBoolean(APP_THEME_STATUS, isDarkThemeEnabled(this))
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
    companion object {
        fun isDarkThemeEnabled(context: Context): Boolean {
            val result =
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> true
                    else -> false
                }
            return result
        }
    }
}

