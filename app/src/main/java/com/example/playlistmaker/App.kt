package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.audio_player.data.impl.MediaPlayerImpl
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.search.data.data_source.TracksLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksRemoteDataSource
import com.example.playlistmaker.search.data.data_source_impl.ListenHistoryTracksLocalDataSourceImpl
import com.example.playlistmaker.search.data.data_source_impl.SearchTracksLocalDataSourceImpl
import com.example.playlistmaker.search.data.data_source_impl.SearchTracksRemoteDataSourceImpl
import com.example.playlistmaker.search.data.mapper.RemoteDatasourceToTrackMapper
import com.example.playlistmaker.search.data.network.RetrofitFactory
import com.example.playlistmaker.search.data.repository_impl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository_impl.SearchRepositoryImpl
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabaseImpl
import com.example.playlistmaker.search.data.storage.TracksSearchCacheImpl
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository
import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.search.domain.use_case.*
import com.example.playlistmaker.settings.data.data_source_impl.SettingsLocalDataSourceImpl
import com.example.playlistmaker.settings.data.repository_impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.repository_impl.ThemeProviderImpl
import com.example.playlistmaker.settings.data.repository_impl.ThemeSetterImpl
import com.example.playlistmaker.settings.data.storage.SettingsLocalDatabaseImpl
import com.example.playlistmaker.settings.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SaveThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SetThemeUseCase
import com.example.playlistmaker.sharing.data.ShareDataSource
import com.example.playlistmaker.sharing.data.ShareResourceRepositoryImpl
import com.example.playlistmaker.sharing.domain.repository.ShareResourceRepository


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val retrofit = RetrofitFactory()
        val itunesService = retrofit.getService()
        val remoteDatasourceToTrackMapper = RemoteDatasourceToTrackMapper()

        val searchTracksRemoteDataSource: TracksRemoteDataSource =
            SearchTracksRemoteDataSourceImpl(itunesService, remoteDatasourceToTrackMapper)

        val tracksSearchLocalDataSource: TracksLocalDataSource =
            SearchTracksLocalDataSourceImpl(TracksSearchCacheImpl)

        val tracksListenHistoryLocalDatabase =
            TracksListenHistoryLocalDatabaseImpl.getInstance(applicationContext)

        val trackListenHistoryLocalDataSource: TracksLocalDataSource =
            ListenHistoryTracksLocalDataSourceImpl(tracksListenHistoryLocalDatabase)

        val searchRepositoryImpl: SearchRepository = SearchRepositoryImpl(
            searchTracksRemoteDataSource,
            tracksSearchLocalDataSource,
        )

        val listenHistoryRepository: ListenHistoryRepository = ListenHistoryRepositoryImpl(
            trackListenHistoryLocalDataSource
        )

        val shareDataSource: ShareDataSource = ShareDataSource.Base(applicationContext)
        shareResourceRepository = ShareResourceRepositoryImpl(shareDataSource)

        val settingsLocalDatabase = SettingsLocalDatabaseImpl.getInstance(applicationContext)
        val settingsLocalDataSource = SettingsLocalDataSourceImpl(settingsLocalDatabase)
        val settingsRepository = SettingsRepositoryImpl(settingsLocalDataSource)
        val themeProvider = ThemeProviderImpl(this)
        val themeSetter = ThemeSetterImpl()


        getThemeUseCase = GetThemeUseCase.Base(settingsRepository, themeProvider)
        setThemeUseCase = SetThemeUseCase.Base(themeSetter)
        saveThemeUseCase = SaveThemeUseCase.Base(settingsRepository)

        searchSongsUseCase = SearchSongsUseCase.Base(searchRepositoryImpl)

        addTracksToListenHistoryUseCase =
            AddTracksToListenHistoryUseCase.Base(listenHistoryRepository)

        clearListenHistoryTracksUseCase =
            ClearListenHistoryTracksUseCase.Base(listenHistoryRepository)

        clearSearchTracksUseCase =
            ClearSearchTracksUseCase.Base(searchRepositoryImpl)

        getSearchResultTracksUseCase =
            GetSearchResultTracksUseCase.Base(searchRepositoryImpl)

        getIsListenHistoryTracksNotEmptyUseCase =
            GetIsListenHistoryTracksNotEmptyUseCase.Base(listenHistoryRepository)

        getListenHistoryTracksUseCase =
            GetListenHistoryTracksUseCase.Base(listenHistoryRepository)

        getIsSearchResultIsEmptyUseCase =
            GetIsSearchResultIsEmptyUseCase.Base(searchRepositoryImpl)

        mediaPlayer = MediaPlayerImpl()

        val isNightTheme = getThemeUseCase.execute()
        setThemeUseCase.execute(isNightTheme)
    }

    companion object {
        lateinit var shareResourceRepository: ShareResourceRepository
        lateinit var getThemeUseCase: GetThemeUseCase
        lateinit var setThemeUseCase: SetThemeUseCase
        lateinit var saveThemeUseCase: SaveThemeUseCase
        lateinit var mediaPlayer: MediaPlayerContract

        lateinit var searchSongsUseCase: SearchSongsUseCase
        lateinit var addTracksToListenHistoryUseCase: AddTracksToListenHistoryUseCase
        lateinit var clearListenHistoryTracksUseCase: ClearListenHistoryTracksUseCase
        lateinit var clearSearchTracksUseCase: ClearSearchTracksUseCase
        lateinit var getSearchResultTracksUseCase: GetSearchResultTracksUseCase
        lateinit var getIsListenHistoryTracksNotEmptyUseCase: GetIsListenHistoryTracksNotEmptyUseCase
        lateinit var getListenHistoryTracksUseCase: GetListenHistoryTracksUseCase
        lateinit var getIsSearchResultIsEmptyUseCase: GetIsSearchResultIsEmptyUseCase
    }
}



