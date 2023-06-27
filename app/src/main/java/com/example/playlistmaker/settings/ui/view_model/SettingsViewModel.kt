package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SaveThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SetThemeUseCase
import com.example.playlistmaker.share.domain.use_case.OpenMailUseCase
import com.example.playlistmaker.share.domain.use_case.OpenLinkUseCase
import com.example.playlistmaker.share.domain.use_case.ShareLinkUseCase

class SettingsViewModel(
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val shareLinkUseCase: ShareLinkUseCase,
    private val openLinkUseCase: OpenLinkUseCase,
    private val openMailUseCase: OpenMailUseCase
) : ViewModel() {

    private val switchCompatState = MutableLiveData<Boolean>()

    init {
        switchCompatState.value = getThemeUseCase.execute()
    }

    fun observeSwitchCompatState(): LiveData<Boolean> = switchCompatState

    fun shareApp() {
        shareLinkUseCase.execute()
    }

    fun openTerms() {
        openLinkUseCase.execute()
    }

    fun writeToSupport() {
        openMailUseCase.execute()
    }

    private fun changeTheme(switchCompatState: Boolean) {
        setThemeUseCase.execute(switchCompatState)
        saveThemeUseCase.execute(switchCompatState)
    }

    fun toggleDarkThemeSwitch(isChecked: Boolean) {
        switchCompatState.value = isChecked
        changeTheme(isChecked)
    }
}