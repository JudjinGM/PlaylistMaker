package com.example.playlistmaker.settings.data.repositoryImpl

import com.example.playlistmaker.settings.data.dataSource.SettingsLocalDataSource
import com.example.playlistmaker.settings.domain.repository.SettingsRepository


class SettingsRepositoryImpl(
    private val dataSource: SettingsLocalDataSource
) : SettingsRepository {
    override fun setSettingBoolean(key: String, value: Boolean) {
        dataSource.setSetting(key, value)
    }

    override fun getSettingBoolean(key: String, default: Boolean): Boolean {
        return dataSource.getSetting(key, default)
    }

}
