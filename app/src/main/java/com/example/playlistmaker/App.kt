package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.local.database.LocalDataSource
import com.example.playlistmaker.data.local.database.LocalDataSource.Companion.APP_THEME_STATUS
import com.example.playlistmaker.data.repositorie.settingRepository.SettingsRepository


class App : Application() {
    var appTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        localDataSource = LocalDataSource.getInstance(applicationContext)
        settingsRepository = SettingsRepository(localDataSource)

        appTheme = settingsRepository.loadBooleanSetting(APP_THEME_STATUS, isDarkThemeEnabled(this))

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
        lateinit var localDataSource: LocalDataSource
        lateinit var settingsRepository: SettingsRepository
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

