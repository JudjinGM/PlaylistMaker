package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.libraries.ThemeProvider
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.SaveThemeUseCase.Companion.APP_THEME_STATUS

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
