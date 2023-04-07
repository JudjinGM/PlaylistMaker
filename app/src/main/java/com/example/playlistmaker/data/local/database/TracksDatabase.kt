package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.Track

class TracksDatabase private constructor(
    val tracksSearch: MutableList<Track>,
    val trackHistory: MutableList<Track>
) {

    companion object {
        private lateinit var instance: TracksDatabase
        fun getInstance(): TracksDatabase {
            if (!::instance.isInitialized) {
                instance = TracksDatabase(mutableListOf(), mutableListOf())
            }
            return instance
        }
    }
}