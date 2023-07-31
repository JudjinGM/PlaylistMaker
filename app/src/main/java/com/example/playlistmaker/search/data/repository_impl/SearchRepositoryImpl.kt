package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.data.data_source.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.data_source.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.mapper.TracksDtoToListTracksMapper
import com.example.playlistmaker.search.data.model.ErrorStatusData
import com.example.playlistmaker.search.data.model.TrackItunesResponse
import com.example.playlistmaker.search.data.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val remoteDataSource: TracksSearchRemoteDataSource,
    private val searchLocalDataSource: TracksSearchLocalDataSource,
    private val mapper: TracksDtoToListTracksMapper
) : SearchRepository {
    override fun searchTracks(
        inputSearchText: String
    ): Flow<Resource<List<Track>>> = flow {
        val response = remoteDataSource.getTracks(inputSearchText)
        when (response.resultCode) {
            200 -> {
                with(response as TrackItunesResponse) {
                    val tracks = mapper.execute(results)
                    searchLocalDataSource.clearAllTracks()
                    searchLocalDataSource.addAllTracks(tracks)
                    emit(Resource.Success(tracks))
                }
            }

            else -> emit(Resource.Error(ErrorStatusData.NO_CONNECTION))
        }
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