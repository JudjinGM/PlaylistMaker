package com.example.playlistmaker.data.dataSources

import com.example.playlistmaker.domain.model.Track

interface TracksLocalDataSource {
    fun getAllTracks(): List<Track>
    fun addAllTracks(listOfTracks: List<Track>)
    fun addTrack(track: Track)
    fun getTrack(id: Long): Track?
    fun deleteTrack(id: Long)
    fun clearAllTracks()
    fun getTracksCount(): Int
}