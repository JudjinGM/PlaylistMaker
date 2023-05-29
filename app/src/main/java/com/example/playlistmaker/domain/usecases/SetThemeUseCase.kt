package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.libraries.ThemeSetter

interface SetThemeUseCase {
    fun execute(isNightThemeEnabled: Boolean)
    class Base(private val themeSetter: ThemeSetter) : SetThemeUseCase {
        override fun execute(isNightThemeEnabled: Boolean) {
            themeSetter.setTheme(isNightThemeEnabled)
        }
    }
}