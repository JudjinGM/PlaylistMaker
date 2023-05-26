package com.example.playlistmaker.domain.usecases

import android.content.Context
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.SaveThemeUseCase.Companion.APP_THEME_STATUS

interface GetThemeUseCase {
    fun execute(): Boolean
    class Base(private val settingsRepository: SettingsRepository, context: Context) :
        GetThemeUseCase {
        private val checkPhoneThemeUseCase = CheckPhoneThemeUseCase.Base()
        private val isDarkThemeEnabled = checkPhoneThemeUseCase.execute(context)

        override fun execute(): Boolean {
            return settingsRepository.getSettingBoolean(APP_THEME_STATUS, isDarkThemeEnabled)
        }
    }
}