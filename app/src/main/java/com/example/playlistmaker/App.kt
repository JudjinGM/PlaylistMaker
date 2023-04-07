package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.local.database.SharedPreferencesDatabase
import com.example.playlistmaker.data.repositorie.BooleanRepository
import com.example.playlistmaker.data.repositorie.SettingsRepository

const val PLAYLIST_PREFS = "playlist_maker_prefs"
const val APP_THEME_STATUS = "dark_theme_status"


class App : Application() {
    var appTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        sharedPreferencesDatabase = SharedPreferencesDatabase.getInstance(applicationContext)
        settingsRepository = SettingsRepository(sharedPreferencesDatabase)

        appTheme = settingsRepository.loadBoolean(APP_THEME_STATUS, isDarkThemeEnabled(this))

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
        lateinit var sharedPreferencesDatabase: SharedPreferencesDatabase
        lateinit var settingsRepository: BooleanRepository
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

