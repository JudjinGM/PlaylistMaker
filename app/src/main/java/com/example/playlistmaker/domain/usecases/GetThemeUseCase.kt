package com.example.playlistmaker.domain.usecases

import android.content.Context
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.SaveThemeUseCase.Companion.APP_THEME_STATUS

class GetThemeUseCase(private val settingsRepository: SettingsRepository, context: Context) {
    private val checkPhoneThemeUseCase = CheckPhoneThemeUseCase()
    private val isDarkThemeEnabled = checkPhoneThemeUseCase.execute(context)

    fun execute() : Boolean{
        return settingsRepository.getSettingBoolean(APP_THEME_STATUS, isDarkThemeEnabled)
    }
}