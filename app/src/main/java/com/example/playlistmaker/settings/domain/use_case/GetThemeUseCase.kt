package com.example.playlistmaker.settings.domain.use_case

import com.example.playlistmaker.settings.domain.repository.ThemeProvider
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.settings.domain.use_case.SaveThemeUseCase.Companion.APP_THEME_STATUS

interface GetThemeUseCase {
    fun execute(): Boolean
    class Base(
        private val settingsRepository: SettingsRepository,
        private val themeProvider: ThemeProvider
    ) :
        GetThemeUseCase {
        override fun execute(): Boolean {
            return settingsRepository.getSettingBoolean(
                APP_THEME_STATUS,
                themeProvider.isNightModeEnabledOnPhone()
            )
        }
    }
}
