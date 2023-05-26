package com.example.playlistmaker.data.repositoryImpl

import com.example.playlistmaker.data.dataSources.TracksLocalDataSource
import com.example.playlistmaker.domain.repository.ListenHistoryRepository
import com.example.playlistmaker.domain.model.Track

class ListenHistoryRepositoryImpl(
    private val localDataSource: TracksLocalDataSource,
) :ListenHistoryRepository {

    override fun getListOfListenHistoryTracks(): List<Track> {
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