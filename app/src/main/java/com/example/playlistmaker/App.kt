package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.data.dataSourceImpl.SettingsLocalDataSourceImpl
import com.example.playlistmaker.data.dataSources.SettingsLocalDataSource
import com.example.playlistmaker.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.data.storage.SettingsLocalDatabase
import com.example.playlistmaker.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.SetThemeUseCase


class App : Application() {
    private lateinit var getThemeUseCase: GetThemeUseCase
    private lateinit var setThemeUseCase: SetThemeUseCase

    override fun onCreate() {
        super.onCreate()

        tracksListenHistoryLocalDatabase =
            TracksListenHistoryLocalDatabase.getInstance(applicationContext)

        settingsLocalDatabase = SettingsLocalDatabase.getInstance(applicationContext)
        settingsLocalDataSource = SettingsLocalDataSourceImpl(settingsLocalDatabase)
        settingsRepository = SettingsRepositoryImpl(settingsLocalDataSource)
        getThemeUseCase = GetThemeUseCase(settingsRepository, this)
        setThemeUseCase = SetThemeUseCase()

        val isDarkTheme = getThemeUseCase.execute()

        setThemeUseCase.execute(isDarkTheme)
    }

    companion object {
        lateinit var tracksListenHistoryLocalDatabase: TracksListenHistoryLocalDatabase
        lateinit var settingsLocalDatabase: SettingsLocalDatabase
        lateinit var settingsLocalDataSource: SettingsLocalDataSource
        lateinit var settingsRepository: SettingsRepositoryImpl
    }
}

