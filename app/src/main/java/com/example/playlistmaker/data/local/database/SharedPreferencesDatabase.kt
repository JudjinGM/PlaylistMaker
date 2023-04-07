package com.example.playlistmaker.data.local.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.PLAYLIST_PREFS
import com.example.playlistmaker.TRACKS_LISTEN_HISTORY
import com.example.playlistmaker.data.model.Track
import com.google.gson.Gson

class SharedPreferencesDatabase private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun loadBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun loadString(key: String, default: String): String? {
        return sharedPreferences.getString(key, default)
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
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

        private lateinit var instance: SharedPreferencesDatabase

        fun getInstance(context: Context): SharedPreferencesDatabase {
            if (!::instance.isInitialized)
                instance = SharedPreferencesDatabase(context)
            return instance
        }
    }
}