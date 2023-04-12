package com.example.playlistmaker.data.local.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.model.Track
import com.google.gson.Gson

class LocalDatabase private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun loadBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun loadListenHistoryTracks(): Array<Track> {
        val json = sharedPreferences.getString(TRACKS_LISTEN_HISTORY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun saveListenHistoryTracks(tracks: Array<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_LISTEN_HISTORY, json)
            .apply()
    }

    companion object {

        private lateinit var instance: LocalDatabase

        fun getInstance(context: Context): LocalDatabase {
            if (!::instance.isInitialized)
                instance = LocalDatabase(context)
            return instance
        }

        const val PLAYLIST_PREFS = "playlist_maker_prefs"
        const val APP_THEME_STATUS = "app_theme_status"
        const val TRACKS_LISTEN_HISTORY = "tracks_listen_history"
    }
}