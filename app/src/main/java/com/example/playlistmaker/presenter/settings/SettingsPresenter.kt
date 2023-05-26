package com.example.playlistmaker.presenter.settings

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.SaveThemeUseCase
import com.example.playlistmaker.domain.usecases.SetThemeUseCase

class SettingsPresenter(private val view: SettingsView, context: Context) {
    private val settingsRepository: SettingsRepository = App.settingsRepository
    private val getThemeUseCase: GetThemeUseCase =
        GetThemeUseCase.Base(settingsRepository, context)
    private val saveThemeUseCase: SaveThemeUseCase = SaveThemeUseCase.Base(settingsRepository)
    private val setAppThemeUseCase: SetThemeUseCase = SetThemeUseCase.Base()

    fun changeTheme(isSwitchCompatChecked: Boolean) {
        setAppThemeUseCase.execute(isSwitchCompatChecked)
        saveThemeUseCase.execute(isSwitchCompatChecked)
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
