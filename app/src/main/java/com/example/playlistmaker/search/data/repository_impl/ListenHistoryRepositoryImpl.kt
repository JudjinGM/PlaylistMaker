package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.data.data_source.TracksListenHistoryLocalDataSource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

class ListenHistoryRepositoryImpl(
    private val listenHistoryLocalDataSource: TracksListenHistoryLocalDataSource,
) : ListenHistoryRepository {

    override fun getListenHistoryTracks(): List<Track> {
        return listenHistoryLocalDataSource.getAllTracks()
    }

    override fun addTrackToListenHistory(track: Track) {
        listenHistoryLocalDataSource.addTrack(track)
    }

    override fun clearListenHistory() {
        listenHistoryLocalDataSource.clearAllTracks()
    }

    override fun isListenHistoryIsNotEmpty(): Boolean {
        return listenHistoryLocalDataSource.getTracksCount() != 0
    }
}