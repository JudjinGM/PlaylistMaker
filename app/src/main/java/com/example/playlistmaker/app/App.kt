package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.audio_player.di.audioPlayerAppModule
import com.example.playlistmaker.audio_player.di.audioPlayerDomainModule
import com.example.playlistmaker.search.di.searchAppModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.settings.di.settingsAppModule
import com.example.playlistmaker.settings.di.settingsDataModule
import com.example.playlistmaker.settings.di.settingsDomainModule
import com.example.playlistmaker.settings.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SetThemeUseCase
import com.example.playlistmaker.sharing.di.sharingDataModule
import com.example.playlistmaker.sharing.di.sharingDomainModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    audioPlayerAppModule,
                    audioPlayerDomainModule,
                    searchAppModule,
                    searchDataModule,
                    searchDomainModule,
                    settingsAppModule,
                    settingsDataModule,
                    settingsDomainModule,
                    sharingDataModule,
                    sharingDomainModule
                )
            )
        }

        val setThemeUseCase: SetThemeUseCase by inject()
        val getThemeUseCase: GetThemeUseCase by inject()
        val isNightTheme = getThemeUseCase.execute()

        setThemeUseCase.execute(isNightTheme)
    }

}



