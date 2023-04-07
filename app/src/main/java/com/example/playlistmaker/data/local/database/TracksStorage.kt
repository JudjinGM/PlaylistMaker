package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.Track

class TracksStorage private constructor(
    val tracksSearch: MutableList<Track>,
) {

    companion object {
        private lateinit var instance: TracksStorage
        fun getInstance(): TracksStorage {
            if (!::instance.isInitialized) {
                instance = TracksStorage(mutableListOf())
            }
            return instance
        }
    }
}