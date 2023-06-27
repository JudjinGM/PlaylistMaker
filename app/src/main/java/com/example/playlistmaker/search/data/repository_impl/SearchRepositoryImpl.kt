package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.data.data_source.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.model.RemoteDatasourceErrorStatus
import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val remoteDataSource: TracksSearchRemoteDataSource,
    private val searchLocalDataSource: TracksSearchLocalDataSource,
) : SearchRepository {
    override fun searchTracks(
        inputSearchText: String, onSuccess: (List<Track>) -> Unit, onError: (ErrorStatus) -> Unit
    ) {
        remoteDataSource.getTracks(inputSearchText, onSuccess = { newTracks ->
            searchLocalDataSource.clearAllTracks()
            searchLocalDataSource.addAllTracks(newTracks)
            onSuccess.invoke(searchLocalDataSource.getAllTracks())
        }, onError = { errorStatus ->
            when (errorStatus) {
                RemoteDatasourceErrorStatus.NOTHING_FOUND -> onError.invoke(ErrorStatus.NOTHING_FOUND)
                RemoteDatasourceErrorStatus.NO_CONNECTION -> onError.invoke(ErrorStatus.NO_CONNECTION)
            }
        })
    }

    override fun addAllTracks(tracks: List<Track>) {
        searchLocalDataSource.addAllTracks(tracks)
    }

    override fun getSearchTracks(): List<Track> {
        return searchLocalDataSource.getAllTracks()
    }

    override fun clearSearchTracks() {
        searchLocalDataSource.clearAllTracks()
    }

    override fun isSearchRepositoryEmpty(): Boolean {
        return searchLocalDataSource.getAllTracks().isEmpty()
    }
}