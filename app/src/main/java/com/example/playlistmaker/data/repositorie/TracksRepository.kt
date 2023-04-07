package com.example.playlistmaker.data.repositorie

import com.example.playlistmaker.data.local.database.TracksDatabase
import com.example.playlistmaker.data.model.*
import com.example.playlistmaker.network.ItunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TracksRepository(
    private val itunesService: ItunesApi,
    private val database: TracksDatabase,
) : DataRepository<Track, Long> {

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
                            val tracks = prepareTracks(body.results)
                            oldTrack.addAll(database.tracksSearch)
                            database.tracksSearch.clear()
                            database.tracksSearch.addAll(tracks)
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

    fun prepareTracks(tracksRaw: MutableList<TrackRaw>): MutableList<Track> {
        val tracks = mutableListOf<Track>()
        for (track in tracksRaw) {
            tracks.add(
                Track(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(track.trackTimeMillis),
                    artworkUrl100 = track.artworkUrl100
                )
            )
        }
        return tracks
    }

    companion object {
        const val RESPONSE_SUCCESS = 200
    }

    override fun saveAllData(dataList: MutableList<Track>) {
        database.tracksSearch.addAll(dataList)
    }

    override fun loadAllData(): MutableList<Track> {
        return database.tracksSearch
    }

    override fun loadData(id: Long): Track? {
        var result: Track? = null
        for (track in database.tracksSearch) {
            if (track.trackId == id) {
                result = track
            }
        }
        return result
    }

    override fun clearDatabase() {
        database.tracksSearch.clear()
    }

    override fun remove(id: Long) {
        for (track in database.tracksSearch) {
            if (track.trackId == id) {
                database.tracksSearch.remove(track)
            }
        }
    }

    override fun saveData(data: Track) {
        database.tracksSearch.add(data)
    }
}