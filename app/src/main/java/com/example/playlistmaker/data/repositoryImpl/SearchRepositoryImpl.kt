package com.example.playlistmaker.data.repositoryImpl
import com.example.playlistmaker.data.dataSources.TracksLocalDataSource
import com.example.playlistmaker.data.dataSources.TracksRemoteDataSource
import com.example.playlistmaker.data.models.RemoteDatasourceErrorStatus
import com.example.playlistmaker.domain.model.RepositoryErrorStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val remoteDataSource: TracksRemoteDataSource,
    private val localDataSource: TracksLocalDataSource,
) : SearchRepository {
    override fun searchTracks(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RepositoryErrorStatus) -> Unit
    ) {
        remoteDataSource.getTracks(inputSearchText, onSuccess = { newTracks ->
            localDataSource.clearAllTracks()
            localDataSource.addAllTracks(newTracks)
            onSuccess.invoke(localDataSource.getAllTracks())
        }, onError = { errorStatus ->
            when (errorStatus) {
                RemoteDatasourceErrorStatus.NOTHING_FOUND -> onError.invoke(RepositoryErrorStatus.NOTHING_FOUND)
                RemoteDatasourceErrorStatus.NO_CONNECTION -> onError.invoke(RepositoryErrorStatus.NO_CONNECTION)
            }
        })
    }

    override fun getSearchTracks(): List<Track> {
        return localDataSource.getAllTracks()
    }

    override fun clearSearchTracks() {
        localDataSource.clearAllTracks()
    }
}