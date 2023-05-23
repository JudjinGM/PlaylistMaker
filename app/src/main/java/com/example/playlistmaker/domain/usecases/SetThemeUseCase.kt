package com.example.playlistmaker.domain.usecases

import androidx.appcompat.app.AppCompatDelegate

class SetThemeUseCase {
    fun execute(darkThemeEnabled: Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}