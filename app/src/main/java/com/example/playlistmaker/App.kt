package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.data.dataSourceImpl.SettingsLocalDataSourceImpl
import com.example.playlistmaker.data.dataSources.SettingsLocalDataSource
import com.example.playlistmaker.data.libraries.ThemeProviderImpl
import com.example.playlistmaker.data.libraries.ThemeSetterImpl
import com.example.playlistmaker.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.data.storage.SettingsLocalDatabase
import com.example.playlistmaker.data.storage.SettingsLocalDatabaseImpl
import com.example.playlistmaker.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.data.storage.TracksListenHistoryLocalDatabaseImpl
import com.example.playlistmaker.domain.libraries.ThemeProvider
import com.example.playlistmaker.domain.libraries.ThemeSetter
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.SetThemeUseCase


class App : Application() {
    private lateinit var getThemeUseCase: GetThemeUseCase
    private lateinit var setThemeUseCase: SetThemeUseCase

    override fun onCreate() {
        super.onCreate()

        tracksListenHistoryLocalDatabase =
            TracksListenHistoryLocalDatabaseImpl.getInstance(applicationContext)

        settingsLocalDatabase = SettingsLocalDatabaseImpl.getInstance(applicationContext)
        settingsLocalDataSource = SettingsLocalDataSourceImpl(settingsLocalDatabase)
        settingsRepository = SettingsRepositoryImpl(settingsLocalDataSource)
        themeProvider = ThemeProviderImpl(this)
        themeSetter = ThemeSetterImpl()
        getThemeUseCase = GetThemeUseCase.Base(settingsRepository, themeProvider)
        setThemeUseCase = SetThemeUseCase.Base(themeSetter)

        val isNightTheme = getThemeUseCase.execute()
        setThemeUseCase.execute(isNightTheme)
    }

    companion object {
        lateinit var tracksListenHistoryLocalDatabase: TracksListenHistoryLocalDatabase
        lateinit var settingsLocalDatabase: SettingsLocalDatabase
        lateinit var settingsLocalDataSource: SettingsLocalDataSource
        lateinit var settingsRepository: SettingsRepository
        lateinit var themeProvider: ThemeProvider
        lateinit var themeSetter: ThemeSetter

    }
}

