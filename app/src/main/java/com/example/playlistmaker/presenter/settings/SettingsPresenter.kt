package com.example.playlistmaker.presenter.settings

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.data.storage.SettingsLocalDatabase
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.SetThemeUseCase

class SettingsPresenter(private val view: SettingsView, context: Context) {

    private val getThemeUseCase =
        GetThemeUseCase(settingsRepository = App.settingsRepository, context)
    private val setThemeUseCase = SetThemeUseCase()

    fun changeTheme(isSwitchCompatChecked: Boolean) {
        setThemeUseCase.execute(isSwitchCompatChecked)
        if (isSwitchCompatChecked) {
            App.settingsRepository.setSettingBoolean(SettingsLocalDatabase.APP_THEME_STATUS, true)
        } else {
            App.settingsRepository.setSettingBoolean(SettingsLocalDatabase.APP_THEME_STATUS, false)
        }
    }

    fun shareApp() {
        view.shareApp()
    }

    fun writeToSupport() {
        view.writeToSupport()
    }

    fun showTermOfUse() {
        view.showTermOfUse()
    }

    fun setSwitchCompatTheme() {
        view.setSwitchCompatTheme(getThemeUseCase.execute())
    }
}
