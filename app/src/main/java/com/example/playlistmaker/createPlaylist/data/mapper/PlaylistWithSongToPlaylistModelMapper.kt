package com.example.playlistmaker.createPlaylist.data.mapper

import android.net.Uri
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

class PlaylistWithSongToPlaylistModelMapper(
    private val trackEntityToTrackMapper: TrackEntityToTrackMapper
) {
    fun execute(playlistWithSongs: PlaylistWithSongs?): PlaylistModel {
        if (playlistWithSongs != null) {
            return PlaylistModel(
                playlistId = playlistWithSongs.playlist.playlistId,
                playlistName = playlistWithSongs.playlist.playlistName,
                playlistDescription = playlistWithSongs.playlist.playlistDescription,
                playlistCoverImage = Uri.parse(playlistWithSongs.playlist.playlistCover),
                tracks = playlistWithSongs.tracks.map { trackEntity ->
                    trackEntityToTrackMapper.execute(trackEntity)
                }
            )
        } else return PlaylistModel(
            playlistId = 0,
            playlistName = "",
            playlistDescription = "",
            playlistCoverImage = Uri.parse(""),
            tracks = emptyList()
        )
    }
}
