package com.example.playlistmaker.settings.data.repository_impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.repository.ThemeSetter

class ThemeSetterImpl : ThemeSetter {
    override fun setTheme(isNightMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}