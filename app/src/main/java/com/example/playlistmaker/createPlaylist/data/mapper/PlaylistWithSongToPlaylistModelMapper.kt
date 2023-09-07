package com.example.playlistmaker.createPlaylist.data.mapper

import android.net.Uri
import android.util.Log
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

class PlaylistWithSongToPlaylistModelMapper(
    private val trackEntityToTrackMapper: TrackEntityToTrackMapper
) {
    fun execute(playlistWithSongs: PlaylistWithSongs): PlaylistModel {
        Log.d("judjin2", playlistWithSongs.playlist.playlistCover)
        return PlaylistModel(
            playlistId = playlistWithSongs.playlist.playlistId,
            playlistName = playlistWithSongs.playlist.playlistName,
            playlistDescription = playlistWithSongs.playlist.playlistDescription,
            playlistCoverImage = Uri.parse(playlistWithSongs.playlist.playlistCover),
            tracks = playlistWithSongs.tracks.map { trackEntity ->
                trackEntityToTrackMapper.execute(trackEntity)
            }
        )
    }
}