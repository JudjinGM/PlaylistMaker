package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.audioPlayer.di.audioPlayerDomainModule
import com.example.playlistmaker.audioPlayer.di.audioPlayerUiModule
import com.example.playlistmaker.createPlaylist.di.createPlaylistDataModule
import com.example.playlistmaker.createPlaylist.di.createPlaylistDomainModule
import com.example.playlistmaker.createPlaylist.di.createPlaylistUiModule
import com.example.playlistmaker.editPlaylist.di.editPlaylistDomainModule
import com.example.playlistmaker.editPlaylist.di.editPlaylistUiModule
import com.example.playlistmaker.library.di.libraryDataModule
import com.example.playlistmaker.library.di.libraryDomainModule
import com.example.playlistmaker.library.di.libraryUiModule
import com.example.playlistmaker.playlist.di.playlistDomainModule
import com.example.playlistmaker.playlist.di.playlistUiModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchUiModule
import com.example.playlistmaker.settings.di.settingsDataModule
import com.example.playlistmaker.settings.di.settingsDomainModule
import com.example.playlistmaker.settings.di.settingsUiModule
import com.example.playlistmaker.settings.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SetThemeUseCase
import com.example.playlistmaker.share.di.shareDataModule
import com.example.playlistmaker.share.di.shareDomainModule
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
                    audioPlayerUiModule,
                    audioPlayerDomainModule,
                    searchUiModule,
                    searchDataModule,
                    searchDomainModule,
                    settingsUiModule,
                    settingsDataModule,
                    settingsDomainModule,
                    shareDataModule,
                    shareDomainModule,
                    libraryDataModule,
                    libraryDomainModule,
                    libraryUiModule,
                    createPlaylistDataModule,
                    createPlaylistDomainModule,
                    createPlaylistUiModule,
                    playlistDomainModule,
                    playlistUiModule,
                    editPlaylistDomainModule,
                    editPlaylistUiModule
                )
            )
        }

        val setThemeUseCase: SetThemeUseCase by inject()
        val getThemeUseCase: GetThemeUseCase by inject()
        val isNightTheme = getThemeUseCase.execute()

        setThemeUseCase.execute(isNightTheme)
    }

}



