package com.example.playlistmaker.search.data.dataSource

import com.example.playlistmaker.search.domain.model.Track

interface TracksListenHistoryLocalDataSource {
    fun getAllTracks(): List<Track>
    fun addAllTracks(listOfTracks: List<Track>)
    fun addTrack(track: Track)
    fun getTrack(id: Long): Track?
    fun deleteTrack(id: Long)
    fun clearAllTracks()
    fun getTracksCount(): Int
    fun updateFavoriteTracks(favoriteTracksId: List<Long>)
}