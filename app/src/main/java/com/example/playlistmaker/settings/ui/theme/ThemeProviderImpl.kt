package com.example.playlistmaker.settings.ui.theme

import android.content.Context
import android.content.res.Configuration
import com.example.playlistmaker.settings.domain.theme.ThemeProvider

class ThemeProviderImpl(private val context: Context) : ThemeProvider {

    override fun isNightModeEnabledOnPhone(): Boolean {
        val result =
            when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        return result
    }

}