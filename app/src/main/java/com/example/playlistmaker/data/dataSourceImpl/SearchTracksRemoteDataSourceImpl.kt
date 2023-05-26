package com.example.playlistmaker.data.dataSourceImpl

import com.example.playlistmaker.data.dataSources.TracksRemoteDataSource
import com.example.playlistmaker.data.mapper.RemoteDatasourceToTrackMapper
import com.example.playlistmaker.data.model.RemoteDatasourceErrorStatus
import com.example.playlistmaker.data.network.ItunesApi
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.model.TrackItunesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTracksRemoteDataSourceImpl(private val itunesService: ItunesApi) :
    TracksRemoteDataSource {

    override fun getTracks(
        query: String, onSuccess: (List<Track>) -> Unit, onError: (RemoteDatasourceErrorStatus) -> Unit
    ) {
        itunesService.search(query).enqueue(object : Callback<TrackItunesResponse> {
            override fun onResponse(
                call: Call<TrackItunesResponse>, response: Response<TrackItunesResponse>
            ) {
                when (response.code()) {
                    RESPONSE_SUCCESS -> {
                        val body: TrackItunesResponse? = response.body()
                        if (body?.results?.isNotEmpty() == true) {

                            val tracks = RemoteDatasourceToTrackMapper().mapTracks(body.results)
                            onSuccess.invoke(tracks)

                        } else onError(RemoteDatasourceErrorStatus.NOTHING_FOUND)
                    }
                    else -> onError(RemoteDatasourceErrorStatus.NO_CONNECTION)
                }
            }

            override fun onFailure(call: Call<TrackItunesResponse>, t: Throwable) {
                onError(RemoteDatasourceErrorStatus.NO_CONNECTION)
            }
        })
    }

    companion object {
        const val RESPONSE_SUCCESS = 200
    }
}