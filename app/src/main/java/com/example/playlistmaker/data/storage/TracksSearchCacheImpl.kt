package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.model.Track

object TracksSearchCacheImpl : TracksSearchCache {
    private val mutableTracks = mutableListOf<Track>()

    override fun addAllTracks(newTracks: List<Track>) {
        mutableTracks.clear()
        mutableTracks.addAll(ArrayList(newTracks))
    }

    override fun getTrackList(): List<Track> {
        return mutableTracks.toList()
    }

    override fun clearTracksCache() {
        mutableTracks.clear()
    }

    override fun removeTrack(track: Track) {
        mutableTracks.remove(track)
    }

    override fun addTrack(track: Track) {
        mutableTracks.add(track)
    }
}
