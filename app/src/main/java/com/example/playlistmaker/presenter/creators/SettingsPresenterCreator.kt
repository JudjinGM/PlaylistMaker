package com.example.playlistmaker.presenter.creators

import com.example.playlistmaker.presenter.settings.SettingsPresenter
import com.example.playlistmaker.presenter.settings.SettingsView

class SettingsPresenterCreator() {
    fun createPresenter(view: SettingsView): SettingsPresenter {
        return SettingsPresenter(view)
    }

}