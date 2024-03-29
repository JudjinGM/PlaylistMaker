package com.example.playlistmaker.settings.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.settings.data.dataSource.SettingsLocalDataSource
import com.example.playlistmaker.settings.data.dataSourceImpl.SettingsLocalDataSourceImpl
import com.example.playlistmaker.settings.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabase
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabaseImpl
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsDataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SettingsLocalDatabaseImpl.SETTINGS_PREFS, Context.MODE_PRIVATE
        )
    }

    singleOf(::SettingsLocalDatabaseImpl) { bind<SettingsLocalDatabase>() }

    singleOf(::SettingsLocalDataSourceImpl) { bind<SettingsLocalDataSource>() }

    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }

}