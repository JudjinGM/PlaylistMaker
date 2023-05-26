package com.example.playlistmaker.presenter.search

import com.example.playlistmaker.App
import com.example.playlistmaker.data.dataSourceImpl.ListenHistoryTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksLocalDataSourceImpl
import com.example.playlistmaker.data.dataSourceImpl.SearchTracksRemoteDataSourceImpl
import com.example.playlistmaker.data.dataSources.TracksLocalDataSource
import com.example.playlistmaker.data.dataSources.TracksRemoteDataSource
import com.example.playlistmaker.data.network.RetrofitFactory
import com.example.playlistmaker.data.repositoryImpl.ListenHistoryRepositoryImpl
import com.example.playlistmaker.data.repositoryImpl.SearchRepositoryImpl
import com.example.playlistmaker.data.storage.TracksSearchCacheImpl
import com.example.playlistmaker.domain.model.PlaceholderStatus
import com.example.playlistmaker.domain.model.RepositoryErrorStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.ListenHistoryRepository
import com.example.playlistmaker.domain.repository.SearchRepository
import com.example.playlistmaker.domain.usecases.*

class SearchPresenter(val view: SearchView) {

    private var retrofit = RetrofitFactory()
    private val itunesService = retrofit.getService()

    private val searchTracksRemoteDataSource: TracksRemoteDataSource =
        SearchTracksRemoteDataSourceImpl(itunesService)

    private val tracksSearchLocalDataSource: TracksLocalDataSource =
        SearchTracksLocalDataSourceImpl(TracksSearchCacheImpl)

    private val trackListenHistoryLocalDataSource: TracksLocalDataSource =
        ListenHistoryTracksLocalDataSourceImpl(App.tracksListenHistoryLocalDatabase)

    private val searchRepositoryImpl: SearchRepository = SearchRepositoryImpl(
        searchTracksRemoteDataSource,
        tracksSearchLocalDataSource,
    )

    private val listenHistoryRepository: ListenHistoryRepository = ListenHistoryRepositoryImpl(
        trackListenHistoryLocalDataSource
    )

    private val searchSongsUseCase: SearchSongsUseCase =
        SearchSongsUseCase.Base(searchRepositoryImpl)

    private val addTracksToListenHistoryUseCase: AddTracksToListenHistoryUseCase =
        AddTracksToListenHistoryUseCase.Base(listenHistoryRepository)

    private val clearListenHistoryUseCase: ClearListenHistoryUseCase =
        ClearListenHistoryUseCase.Base(listenHistoryRepository)

    private val clearSearchListUseCase: ClearSearchListUseCase =
        ClearSearchListUseCase.Base(searchRepositoryImpl)

    private val getSearchResultTrackListUseCase: GetSearchResultTrackListUseCase =
        GetSearchResultTrackListUseCase.Base(searchRepositoryImpl)

    private val getIsListenTrackListIsNotEmptyUseCase: GetIsListenTrackListIsNotEmptyUseCase =
        GetIsListenTrackListIsNotEmptyUseCase.Base(listenHistoryRepository)

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
        return getIsListenTrackListIsNotEmptyUseCase.execute()
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