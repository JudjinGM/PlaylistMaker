package com.example.playlistmaker.search.data.repositoryImpl

import com.example.playlistmaker.library.data.dataSource.FavoriteTracksDataSource
import com.example.playlistmaker.search.data.dataSource.TracksListenHistoryLocalDataSource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

class ListenHistoryRepositoryImpl(
    private val listenHistoryLocalDataSource: TracksListenHistoryLocalDataSource,
    private val favoriteTracksDataSource: FavoriteTracksDataSource
) : ListenHistoryRepository {

    override suspend fun getListenHistoryTracks(): List<Track> {
        val favoriteTracksId = favoriteTracksDataSource.getAllFavoriteTrackId()
        listenHistoryLocalDataSource.updateFavoriteTracks(favoriteTracksId)
        return listenHistoryLocalDataSource.getAllTracks()
    }

    override fun updateFavoriteListenHistory(favoriteTracksId:List<Long>) {
        listenHistoryLocalDataSource.updateFavoriteTracks(favoriteTracksId)
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