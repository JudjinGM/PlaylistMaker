package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.model.Track

object TracksSearchCache {
    private val mutableTracks = mutableListOf<Track>()
    val trackList: List<Track>
        get() = mutableTracks.toList()
    
    fun addAllTracks(newTracks: List<Track>) {
        mutableTracks.clear()
        mutableTracks.addAll(ArrayList(newTracks))
    }

    fun clearTracksCache() {
        mutableTracks.clear()
    }

    fun removeTrack(track: Track) {
        mutableTracks.remove(track)
    }

    fun addTrack(track: Track) {
        mutableTracks.add(track)
    }
}
