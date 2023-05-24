package com.example.playlistmaker.presenter.settings

interface SettingsView {
    fun shareApp()
    fun writeToSupport()
    fun showTermOfUse()
    fun setSwitchCompatTheme(isChecked:Boolean)
}