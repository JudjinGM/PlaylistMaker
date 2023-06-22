package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.data_source.SettingsLocalDataSource
import com.example.playlistmaker.settings.data.data_source_impl.SettingsLocalDataSourceImpl
import com.example.playlistmaker.settings.data.repository_impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabase
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabaseImpl
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import org.koin.dsl.module

val settingsDataModule = module {
    single<SettingsLocalDatabase> {
        SettingsLocalDatabaseImpl(context = get())
    }

    single<SettingsLocalDataSource> {
        SettingsLocalDataSourceImpl(database = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(dataSource = get())
    }

}