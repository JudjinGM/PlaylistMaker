package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson

class TracksListenHistoryLocalDatabaseImpl(private val playlistSharedPreferences: SharedPreferences) :
    TracksListenHistoryLocalDatabase {

    override fun getListenHistoryTrackList(): List<Track> {
        val json =
            playlistSharedPreferences.getString(TRACKS_LISTEN_HISTORY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun addListenHistoryTracks(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        playlistSharedPreferences.edit()
            .putString(TRACKS_LISTEN_HISTORY, json)
            .apply()
    }

    override fun updateTracks(favoriteTracksId: List<Long>) {
        val tracks = getListenHistoryTrackList().onEach { track ->
            track.isFavorite = favoriteTracksId.contains(track.trackId)
        }
        addListenHistoryTracks(tracks)
    }

    companion object {
        const val PLAYLIST_PREFS = "playlist_maker_prefs"
        const val TRACKS_LISTEN_HISTORY = "tracks_listen_history"
    }
}