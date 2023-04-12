package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.Track

object TracksSearchStorage {
    private val tracksMutable = mutableListOf<Track>()
    val tracks: List<Track>
        get()=
        tracksMutable.toList()


    fun saveAll(newTracks: List<Track>) {
        tracksMutable.clear()
        tracksMutable.addAll(ArrayList(newTracks))
    }

    fun clearTrackStorage() {
        tracksMutable.clear()
    }

    fun remove(track: Track) {
       tracksMutable.remove(track)
    }

    fun add(track: Track) {
        tracksMutable.add(track)
    }
}
