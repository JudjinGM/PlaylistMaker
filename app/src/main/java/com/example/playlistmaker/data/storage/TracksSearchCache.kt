package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.model.Track

interface TracksSearchCache {
    fun addAllTracks(newTracks: List<Track>)
    fun getTrackList():List<Track>
    fun clearTracksCache()
    fun removeTrack(track: Track)
    fun addTrack(track: Track)
}