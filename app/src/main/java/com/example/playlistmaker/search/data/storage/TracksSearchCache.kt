package com.example.playlistmaker.search.data.storage

import com.example.playlistmaker.search.domain.model.Track

interface TracksSearchCache {
    fun addAllTracks(newTracks: List<Track>)
    fun getTrackList(): List<Track>
    fun clearTracksCache()
    fun removeTrack(track: Track)
    fun addTrack(track: Track)
}