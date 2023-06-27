package com.example.playlistmaker.settings.domain.use_case

import com.example.playlistmaker.settings.domain.theme.ThemeSetter

interface SetThemeUseCase {
    fun execute(isNightThemeEnabled: Boolean)
    class Base(
        private val themeSetter: ThemeSetter
    ) : SetThemeUseCase {
        override fun execute(isNightThemeEnabled: Boolean) {
            themeSetter.setTheme(isNightThemeEnabled)
        }
    }
}