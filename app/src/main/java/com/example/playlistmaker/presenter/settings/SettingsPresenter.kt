package com.example.playlistmaker.presenter.settings

import com.example.playlistmaker.App
import com.example.playlistmaker.domain.libraries.ThemeProvider
import com.example.playlistmaker.domain.libraries.ThemeSetter
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.SaveThemeUseCase
import com.example.playlistmaker.domain.usecases.SetThemeUseCase

class SettingsPresenter(private val view: SettingsView) {
    private val settingsRepository: SettingsRepository = App.settingsRepository
    private val themeProvider: ThemeProvider = App.themeProvider
    private val themeSetter: ThemeSetter = App.themeSetter

    private val getThemeUseCase: GetThemeUseCase =
        GetThemeUseCase.Base(settingsRepository, themeProvider)
    private val saveThemeUseCase: SaveThemeUseCase = SaveThemeUseCase.Base(settingsRepository)
    private val setAppThemeUseCase: SetThemeUseCase = SetThemeUseCase.Base(themeSetter)

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
