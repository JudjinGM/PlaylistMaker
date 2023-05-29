package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.SettingsRepository

interface SaveThemeUseCase {
    fun execute(isNightTheme: Boolean)
    class Base(private val settingsRepository: SettingsRepository) : SaveThemeUseCase {
        override fun execute(isNightTheme: Boolean) {
            if (isNightTheme) {
                settingsRepository.setSettingBoolean(APP_THEME_STATUS, true)
            } else {
                settingsRepository.setSettingBoolean(APP_THEME_STATUS, false)
            }
        }
    }
    companion object {
        const val APP_THEME_STATUS = "app_theme_status"
    }
}