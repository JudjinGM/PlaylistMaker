package com.example.playlistmaker.data.repositorie

import com.example.playlistmaker.data.local.database.TracksDatabase
import com.example.playlistmaker.data.model.Track

class TrackHistoryRepository(private val database: TracksDatabase):DataRepository<Track, Long> {
    override fun saveAllData(dataList: MutableList<Track>) {
        database.trackHistory.addAll(dataList)
    }

    override fun loadAllData(): MutableList<Track> {
        return database.trackHistory
    }

    override fun loadData(key: Long): Track? {
        var result: Track? = null
        for (track in database.trackHistory) {
            if (track.trackId == key) {
                result = track
            }
        }
        return result
    }

    override fun clearDatabase() {
        database.trackHistory.clear()
    }

    override fun removeData(key: Long) {
        for (track in database.trackHistory) {
            if (track.trackId == key) {
                database.trackHistory.remove(track)
            }
        }
    }

    override fun saveData(data: Track) {
        database.trackHistory.add(data)
    }

}