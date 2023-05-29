package com.example.playlistmaker.data.libraries

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.libraries.ThemeSetter

class ThemeSetterImpl: ThemeSetter {
    override fun setTheme(isNightMode:Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}