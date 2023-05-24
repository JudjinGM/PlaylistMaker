package com.example.playlistmaker.presenter

import com.example.playlistmaker.App
import com.example.playlistmaker.data.dataSourceImpl.ListenHistoryTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksRemoteDataSourceImpl
import com.example.playlistmaker.data.models.RemoteDatasourceErrorStatus
import com.example.playlistmaker.data.network.RetrofitFactory
import com.example.playlistmaker.data.repositoryImpl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.data.repositoryImpl.SearchRepositoryImpl
import com.example.playlistmaker.data.storage.TracksSearchCache
import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.Track

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

    fun searchRequest(inputSearchText: String) {
        searchRepositoryImpl.searchTracks(inputSearchText, { newTracks ->
            view.updateAdapter(newTracks)
            view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
        }, { errorStatus ->
            when (errorStatus) {
                RemoteDatasourceErrorStatus.NOTHING_FOUND -> {
                    searchRepositoryImpl.clearSearchTracks()
                    view.updateAdapter(searchRepositoryImpl.getSearchTracks())
                    view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_NOTHING_FOUND)
                }
                RemoteDatasourceErrorStatus.NO_CONNECTION -> {
                    searchRepositoryImpl.clearSearchTracks()
                    view.updateAdapter(searchRepositoryImpl.getSearchTracks())
                    view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_NO_CONNECTION)}
            }
        })
        view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_PROGRESS_BAR)
    }

    fun provideIsListenHistoryNotEmpty(): Boolean {
        return listenHistoryRepository.isListenHistoryIsNotEmpty()
    }

    fun showListenHistory() {
        view.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
        view.showPlaceholder(PlaceholderStatus.PLACEHOLDER_HISTORY)
    }

    fun showEmptyListenHistory(){
        listenHistoryRepository.clearListenHistory()
        view.updateAdapter(listenHistoryRepository.getListOfListenHistoryTracks())
        view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    fun addTrackToListenHistory(track: Track){
        listenHistoryRepository.addTrackToListenHistory(track)
    }

    fun showEmptySearch() {
        searchRepositoryImpl.clearSearchTracks()
        view.updateAdapter(searchRepositoryImpl.getSearchTracks())
        view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    fun showSearchedTracks() {
        view.updateAdapter(searchRepositoryImpl.getSearchTracks())
        view.showPlaceholder(PlaceholderStatus.NO_PLACEHOLDER)
    }

    fun provideSearchTracks(): List<Track> {
        return searchRepositoryImpl.getSearchTracks()
    }


}