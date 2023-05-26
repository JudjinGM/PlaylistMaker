package com.example.playlistmaker.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

class TracksListenHistoryLocalDatabaseImpl private constructor(context: Context):TracksListenHistoryLocalDatabase {

    private val playlistSharedPreferences = context.getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)

    override fun getListenHistoryMutableTrackList(): MutableList<Track> {
        val json = playlistSharedPreferences.getString(TRACKS_LISTEN_HISTORY, null) ?: return mutableListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }

    override fun addListenHistoryTracks(tracks: Array<Track>) {
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