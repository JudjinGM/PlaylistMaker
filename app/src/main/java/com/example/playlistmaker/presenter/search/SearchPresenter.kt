package com.example.playlistmaker.presenter.search

import com.example.playlistmaker.App
import com.example.playlistmaker.data.dataSourceImpl.ListenHistoryTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksRemoteDataSourceImpl
import com.example.playlistmaker.data.network.RetrofitFactory
import com.example.playlistmaker.data.repositoryImpl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.data.repositoryImpl.SearchRepositoryImpl
import com.example.playlistmaker.data.storage.TracksSearchCache
import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.RepositoryErrorStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecases.*

class SearchPresenter(val view: SearchView) {

    private var retrofit = RetrofitFactory()
    private val itunesService = retrofit.getService()

    private val tracksSearchCache = TracksSearchCache
    private val searchTracksRemoteDataSource = SearchTracksRemoteDataSourceImpl(itunesService)
    private val tracksSearchLocalDataSource = SearchTracksLocalDataSourceImpl(tracksSearchCache)
    private val trackListenHistoryLocalDataSource =
        ListenHistoryTracksLocalDataSourceImpl(App.tracksListenHistoryLocalDatabase)

    private val searchRepositoryImpl = SearchRepositoryImpl(
        searchTracksRemoteDataSource,
        tracksSearchLocalDataSource,
    )

    private val listenHistoryRepository = ListenHistoryRepositoryImpl(
        trackListenHistoryLocalDataSource
    )

    private val searchSongsUseCase = SearchSongsUseCase(searchRepositoryImpl)
    private val addTracksToListenHistoryUseCase =
        AddTracksToListenHistoryUseCase(listenHistoryRepository)
    private val clearListenHistoryUseCase = ClearListenHistoryUseCase(listenHistoryRepository)
    private val clearSearchListUseCase = ClearSearchListUseCase(searchRepositoryImpl)
    private val getSearchResultTrackListUseCase = GetSearchResultTrackListUseCase(searchRepositoryImpl)
    private val getIsListenTrackListIsNotEmpty =
        GetIsListenTrackListIsNotEmpty(listenHistoryRepository)

    fun searchRequest(inputSearchText: String) {
        searchSongsUseCase.execute(inputSearchText, onSuccess = { tracks ->
            view.updateAdapter(tracks)
            view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
        }, onError = { errorStatus ->
            when (errorStatus) {
                RepositoryErrorStatus.NOTHING_FOUND -> {
                    clearSearchListUseCase.execute()
                    view.updateAdapter(searchRepositoryImpl.getSearchTracks())
                    view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_NOTHING_FOUND)
                }
                RepositoryErrorStatus.NO_CONNECTION -> {
                    clearSearchListUseCase.execute()
                    view.updateAdapter(searchRepositoryImpl.getSearchTracks())
                    view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_NO_CONNECTION)
                }
            }
        })
        view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_PROGRESS_BAR)
    }

    fun provideIsListenHistoryNotEmpty(): Boolean {
        return getIsListenTrackListIsNotEmpty.execute()
    }

    fun showListenHistory() {
        view.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
        view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_HISTORY)
    }

    fun showEmptyListenHistory() {
        clearListenHistoryUseCase.execute()
        view.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
        view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    fun addTrackToListenHistory(track: Track) {
        addTracksToListenHistoryUseCase.execute(track)
    }

    fun showEmptySearch() {
        clearSearchListUseCase.execute()
        view.updateAdapter(searchRepositoryImpl.getSearchTracks())
        view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    fun showSearchedTracks() {
        view.updateAdapter(searchRepositoryImpl.getSearchTracks())
        view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    fun provideSearchTracks(): List<Track> {
        return getSearchResultTrackListUseCase.execute()
    }

}