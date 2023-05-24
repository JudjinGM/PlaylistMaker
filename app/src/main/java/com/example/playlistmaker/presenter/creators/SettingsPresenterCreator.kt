package com.example.playlistmaker.presenter.creators

import android.content.Context
import com.example.playlistmaker.presenter.settings.SettingsPresenter
import com.example.playlistmaker.presenter.settings.SettingsView

class SettingsPresenterCreator(val context: Context) {
    fun createPresenter(view: SettingsView): SettingsPresenter {
        return SettingsPresenter(view, context)
    }

}