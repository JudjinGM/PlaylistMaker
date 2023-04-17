package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.Track

interface DataSource {
    fun loadAllTracks(): List<Track>
    fun saveAllTracks(listOfTracks: List<Track>)
    fun saveTrack(track: Track)
    fun loadTrack(id: Long): Track?
    fun removeTrack(id: Long)
    fun clearDatabase()
}