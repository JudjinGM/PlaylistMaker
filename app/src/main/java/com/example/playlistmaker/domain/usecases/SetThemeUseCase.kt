package com.example.playlistmaker.domain.usecases

import androidx.appcompat.app.AppCompatDelegate

interface SetThemeUseCase {
    fun execute(darkThemeEnabled: Boolean)
    class Base : SetThemeUseCase {
        override fun execute(darkThemeEnabled: Boolean) {
            AppCompatDelegate.setDefaultNightMode(
                if (darkThemeEnabled) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }
}