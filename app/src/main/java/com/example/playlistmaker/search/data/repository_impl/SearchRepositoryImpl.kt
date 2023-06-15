package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.data.data_source.TracksLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksRemoteDataSource
import com.example.playlistmaker.search.data.model.RemoteDatasourceErrorStatus
import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val remoteDataSource: TracksRemoteDataSource,
    private val localDataSource: TracksLocalDataSource,
) : SearchRepository {
    override fun searchTracks(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (ErrorStatus) -> Unit
    ) {
        remoteDataSource.getTracks(inputSearchText, onSuccess = { newTracks ->
            localDataSource.clearAllTracks()
            localDataSource.addAllTracks(newTracks)
            onSuccess.invoke(localDataSource.getAllTracks())
        }, onError = { errorStatus ->
            when (errorStatus) {
                RemoteDatasourceErrorStatus.NOTHING_FOUND -> onError.invoke(ErrorStatus.NOTHING_FOUND)
                RemoteDatasourceErrorStatus.NO_CONNECTION -> onError.invoke(ErrorStatus.NO_CONNECTION)
            }
        })
    }

    override fun addAllTracks(tracks: List<Track>) {
        localDataSource.addAllTracks(tracks)
    }

    override fun getSearchTracks(): List<Track> {
        return localDataSource.getAllTracks()
    }

    override fun clearSearchTracks() {
        localDataSource.clearAllTracks()
    }

    override fun isSearchRepositoryEmpty(): Boolean {
        return localDataSource.getAllTracks().isEmpty()
    }
}