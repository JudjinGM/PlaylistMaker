package com.example.playlistmaker.domain.usecases

import android.content.Context
import com.example.playlistmaker.data.storage.SettingsLocalDatabase.Companion.APP_THEME_STATUS
import com.example.playlistmaker.domain.repository.SettingsRepository

class GetThemeUseCase(private val settingsRepository: SettingsRepository, context: Context) {
    private val checkPhoneTheme = CheckPhoneTheme()
    private val isDarkThemeEnabled = checkPhoneTheme.execute(context)

    fun execute() : Boolean{
        return settingsRepository.getSettingBoolean(APP_THEME_STATUS, isDarkThemeEnabled)
    }
}