package com.example.playlistmaker.data.repositoryImpl

import com.example.playlistmaker.data.dataSources.SettingsLocalDataSource
import com.example.playlistmaker.domain.repository.SettingsRepository


class SettingsRepositoryImpl(private val dataSource: SettingsLocalDataSource) : SettingsRepository {
    override fun setSettingBoolean(key: String, value: Boolean) {
        dataSource.setSetting(key, value)
    }

    override fun getSettingBoolean(key: String, default: Boolean): Boolean {
        return dataSource.getSetting(key, default)
    }

}
