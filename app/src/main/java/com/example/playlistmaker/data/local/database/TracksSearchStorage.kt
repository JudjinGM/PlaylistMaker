package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.Track

object TracksSearchStorage {
    private var tracks: List<Track> = listOf()

    fun getAll(): List<Track>{
        return tracks
    }

    fun saveAll(newTracks: List<Track>)
    {
        tracks = newTracks
    }
    fun clearTrackStorage(){
        tracks = listOf()
    }

    fun remove(track: Track){
        val tempTracks = tracks.toMutableList()
        tempTracks.remove(track)
        this.tracks = tempTracks
    }

    fun add(track: Track){
        val tempTracks = tracks.toMutableList()
        tempTracks.add(track)
        this.tracks = tempTracks
    }
}
