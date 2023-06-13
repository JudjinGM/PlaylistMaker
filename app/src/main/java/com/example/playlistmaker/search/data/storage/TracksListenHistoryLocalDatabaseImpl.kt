package com.example.playlistmaker.search.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson

class TracksListenHistoryLocalDatabaseImpl private constructor(context: Context):
    TracksListenHistoryLocalDatabase {

    private val playlistSharedPreferences = context.getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)

    override fun getListenHistoryTrackList(): List<Track> {
        val json = playlistSharedPreferences.getString(TRACKS_LISTEN_HISTORY, null) ?: return listOf()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun addListenHistoryTracks(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        playlistSharedPreferences.edit()
            .putString(TRACKS_LISTEN_HISTORY, json)
            .apply()
    }

    companion object {

        private lateinit var instance: TracksListenHistoryLocalDatabaseImpl
        fun getInstance(context: Context): TracksListenHistoryLocalDatabaseImpl {
            if (!Companion::instance.isInitialized)
                instance = TracksListenHistoryLocalDatabaseImpl(context)
            return instance
        }

        const val PLAYLIST_PREFS = "playlist_maker_prefs"
        const val TRACKS_LISTEN_HISTORY = "tracks_listen_history"
    }
}