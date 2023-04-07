package com.example.playlistmaker.data.repositorie

import com.example.playlistmaker.data.local.database.SharedPreferencesDatabase
import com.example.playlistmaker.data.model.Track

class TrackListenHistoryRepository(
    private val sharedPreferencesDatabase: SharedPreferencesDatabase
) : DataRepository<Track, Long> {
    override fun loadAllData(): MutableList<Track> {
        return sharedPreferencesDatabase.loadListenHistoryTracks().toMutableList()
    }

    override fun clearDatabase() {
        val tracks = sharedPreferencesDatabase.loadListenHistoryTracks().toMutableList()
        tracks.clear()
        sharedPreferencesDatabase.saveListenHistoryTracks(tracks.toTypedArray())
    }

    override fun removeData(key: Long) {
        val tracks = sharedPreferencesDatabase.loadListenHistoryTracks().toMutableList()
        for (track in tracks) {
            if (track.trackId == key) tracks.remove(track)
        }
        sharedPreferencesDatabase.saveListenHistoryTracks(tracks.toTypedArray())
    }

    override fun loadData(key: Long): Track? {
        val tracks = sharedPreferencesDatabase.loadListenHistoryTracks().toMutableList()
        var result: Track? = null
        for (track in tracks) {
            if (track.trackId == key) result = track
        }
        return result
    }

    override fun saveData(data: Track) {
        val tracks: MutableList<Track> = sharedPreferencesDatabase.loadListenHistoryTracks().toMutableList()
        val result = mutableListOf<Track>()
        result.addAll(tracks)
        if (tracks.isNotEmpty()) {
            for (track in tracks) {
                if (data.trackId != track.trackId && tracks.size < 10) {
                        result.add(data)
                    } else if(data.trackId != track.trackId) {
                        result.removeAt(0)
                        result.add(data)
                    }
                }
            } else result.add(data)
        sharedPreferencesDatabase.saveListenHistoryTracks(result.toTypedArray())
    }

    fun getSize(): Int {
        return sharedPreferencesDatabase.loadListenHistoryTracks().toMutableList().size
    }

}