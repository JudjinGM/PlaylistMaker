package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.data.data_source.TracksLocalDataSource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

class ListenHistoryRepositoryImpl(
    private val localDataSource: TracksLocalDataSource,
) : ListenHistoryRepository {

    override fun getListenHistoryTracks(): List<Track> {
        return localDataSource.getAllTracks()
    }

    override fun addTrackToListenHistory(track: Track) {
        localDataSource.addTrack(track)
    }

    override fun clearListenHistory() {
        localDataSource.clearAllTracks()
    }

    override fun isListenHistoryIsNotEmpty(): Boolean {
        return localDataSource.getTracksCount() != 0
    }
}