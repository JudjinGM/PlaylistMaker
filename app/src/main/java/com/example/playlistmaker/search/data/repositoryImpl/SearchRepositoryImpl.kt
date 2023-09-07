package com.example.playlistmaker.search.data.repositoryImpl

import com.example.playlistmaker.library.data.dataSource.FavoriteTracksDataSource
import com.example.playlistmaker.search.data.dataSource.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.dataSource.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.mapper.TracksDtoToListTracksMapper
import com.example.playlistmaker.search.data.model.ErrorStatusData
import com.example.playlistmaker.search.data.model.Resource
import com.example.playlistmaker.search.data.model.TrackItunesResponse
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val remoteDataSource: TracksSearchRemoteDataSource,
    private val searchLocalDataSource: TracksSearchLocalDataSource,
    private val mapper: TracksDtoToListTracksMapper,
    private val favoriteTrackDataSource: FavoriteTracksDataSource
) : SearchRepository {

    override fun searchTracks(
        inputSearchText: String
    ): Flow<Resource<List<Track>>> = flow {
        val response = remoteDataSource.getTracks(inputSearchText)
        when (response.resultCode) {
            200 -> {
                with(response as TrackItunesResponse) {
                    val tracks = mapper.execute(results)
                    tracks.onEach { track ->
                        track.isFavorite =
                            favoriteTrackDataSource.getAllFavoriteTrackId().contains(track.trackId)
                    }
                    emit(Resource.Success(tracks))
                }
            }

            else -> emit(Resource.Error(ErrorStatusData.NO_CONNECTION))
        }
    }

    override fun addAllTracks(tracks: List<Track>) {
        searchLocalDataSource.addAllTracks(tracks)
    }

    override suspend fun getSearchTracks(): List<Track> {
        val tracks = searchLocalDataSource.getAllTracks()
        return tracks.onEach { track ->
            track.isFavorite =
                favoriteTrackDataSource.getAllFavoriteTrackId().contains(track.trackId)
        }
    }

    override fun clearSearchTracks() {
        searchLocalDataSource.clearAllTracks()
    }

    override fun isSearchRepositoryEmpty(): Boolean {
        return searchLocalDataSource.getAllTracks().isEmpty()
    }

    override fun updateSearchResultFavorite(favoriteTracks: List<Long>) {
        searchLocalDataSource.updateSearchLocalDatasource(favoriteTracks)
    }
}