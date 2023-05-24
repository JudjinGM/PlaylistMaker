package com.example.playlistmaker.data.repositoryImpl

import com.example.playlistmaker.data.dataSources.TracksLocalDataSource
import com.example.playlistmaker.data.dataSources.TracksRemoteDataSource
import com.example.playlistmaker.data.models.RemoteDatasourceErrorStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val remoteDataSource: TracksRemoteDataSource,
    private val localDataSource: TracksLocalDataSource,
) : SearchRepository {
    override fun searchTracks(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RemoteDatasourceErrorStatus) -> Unit
    ) {
        remoteDataSource.getTracks(inputSearchText, onSuccess = { newTracks ->
            localDataSource.clearAllTracks()
            localDataSource.addAllTracks(newTracks)
            onSuccess.invoke(localDataSource.getAllTracks())
        }, onError = { errorStatus -> onError.invoke(errorStatus) })
    }

    override fun getSearchTracks(): List<Track> {
        return localDataSource.getAllTracks()
    }

    override fun clearSearchTracks() {
        localDataSource.clearAllTracks()
    }
}