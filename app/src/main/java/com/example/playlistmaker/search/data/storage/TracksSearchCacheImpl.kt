package com.example.playlistmaker.search.data.storage

import com.example.playlistmaker.search.domain.model.Track

class TracksSearchCacheImpl() : TracksSearchCache {
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

    override fun updateTracks(favoriteTracksId: List<Long>) {
        mutableTracks.onEach {
            it.isFavorite = favoriteTracksId.contains(it.trackId)
        }
    }
}
