package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SaveThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SetThemeUseCase
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.use_case.OpenSupportUseCase
import com.example.playlistmaker.sharing.domain.use_case.OpenTermsUseCase
import com.example.playlistmaker.sharing.domain.use_case.ShareAppUseCase

class SettingsViewModel(
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val externalNavigator: ExternalNavigator,
) : ViewModel() {



    private val shareAppUseCase: ShareAppUseCase = ShareAppUseCase.Base(externalNavigator)
    private val openTermsUseCase: OpenTermsUseCase = OpenTermsUseCase.Base(externalNavigator)
    private val openSupportUseCase: OpenSupportUseCase = OpenSupportUseCase.Base(externalNavigator)

    private val switchCompatState = MutableLiveData<Boolean>()

    init {
        switchCompatState.value = getThemeUseCase.execute()
    }

    fun observeSwitchCompatState(): LiveData<Boolean> = switchCompatState

    private fun changeTheme(switchCompatState: Boolean) {
        setThemeUseCase.execute(switchCompatState)
        saveThemeUseCase.execute(switchCompatState)
    }

    fun shareApp() {
        shareAppUseCase.execute()
    }

    fun openTerms() {
        openTermsUseCase.execute()
    }

    fun openSupport() {
        openSupportUseCase.execute()
    }

    fun toggleDarkThemeSwitch(isChecked: Boolean) {
        switchCompatState.value = isChecked
        changeTheme(isChecked)
    }

    companion object {
        fun getViewModelFactory(externalNavigator: ExternalNavigator): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    getThemeUseCase = App.getThemeUseCase,
                    setThemeUseCase = App.setThemeUseCase,
                    saveThemeUseCase = App.saveThemeUseCase,
                    externalNavigator = externalNavigator
                )
            }
        }
    }
}