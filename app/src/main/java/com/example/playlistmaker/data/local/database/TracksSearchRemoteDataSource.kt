package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.*
import com.example.playlistmaker.network.ItunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TracksSearchRemoteDataSource(private val itunesService: ItunesApi) {

    fun search(query: String, callbackUpdate: CallbackUpdate, callbackShow: CallbackShow) {
        itunesService.search(query).enqueue(object : Callback<TrackItunesResponse> {
            override fun onResponse(
                call: Call<TrackItunesResponse>, response: Response<TrackItunesResponse>
            ) {
                when (response.code()) {
                    RESPONSE_SUCCESS -> {
                        val body: TrackItunesResponse? = response.body()
                        if (body?.results?.isNotEmpty() == true) {
                            val tracks = prepareTracks(body.results)
                            callbackUpdate.update(tracks)
                            callbackShow.show(PlaceholderStatus.NO_PLACEHOLDER)

                        } else callbackShow.show(PlaceholderStatus.PLACEHOLDER_NOTHING_FOUND)
                    }
                    else -> callbackShow.show(PlaceholderStatus.NO_CONNECTION)
                }
            }

            override fun onFailure(call: Call<TrackItunesResponse>, t: Throwable) {
                callbackShow.show(PlaceholderStatus.NO_CONNECTION)
            }
        })
    }

    fun prepareTracks(tracksRaw: MutableList<TrackRaw>): MutableList<Track> {
        val tracks = mutableListOf<Track>()
        for (track in tracksRaw) {
            tracks.add(
                Track(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    country = track.country,
                    primaryGenreName = track.primaryGenreName
                )
            )
        }
        return tracks
    }

    companion object {
        const val RESPONSE_SUCCESS = 200
    }
}