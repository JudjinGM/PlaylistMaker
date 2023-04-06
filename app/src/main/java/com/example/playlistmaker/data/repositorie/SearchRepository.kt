package com.example.playlistmaker.data.repositorie

import com.example.playlistmaker.data.local.database.TracksDatabase
import com.example.playlistmaker.data.model.*
import com.example.playlistmaker.network.ItunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(
    private val itunesService: ItunesApi,
    private val database: TracksDatabase,
) {

    fun search(query: String, callbackUpdate: CallbackUpdate, callbackShow: CallbackShow) {
        itunesService.search(query).enqueue(object : Callback<TrackItunesResponse> {
            override fun onResponse(
                call: Call<TrackItunesResponse>, response: Response<TrackItunesResponse>
            ) {
                when (response.code()) {
                    RESPONSE_SUCCESS -> {
                        val body: TrackItunesResponse? = response.body()
                        if (body?.results?.isNotEmpty() == true) {
                            val oldTrack = mutableListOf<Track>()
                            oldTrack.addAll(database.tracks)
                            database.tracks.clear()
                            database.tracks.addAll(body.results)
                            callbackUpdate.update(oldTrack)
                            callbackShow.show(ResponseStatusCodes.OK)
                        } else callbackShow.show(ResponseStatusCodes.NOTHING_FOUND)
                    }
                    else -> callbackShow.show(ResponseStatusCodes.NO_CONNECTION)
                }
            }

            override fun onFailure(call: Call<TrackItunesResponse>, t: Throwable) {
                callbackShow.show(ResponseStatusCodes.NO_CONNECTION)
            }
        })
    }

    fun getTracks(): MutableList<Track> {
        return database.tracks
    }

    fun clearTracks() {
        database.tracks.clear()
    }

    companion object {
        const val RESPONSE_SUCCESS = 200
    }

}