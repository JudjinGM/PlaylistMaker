package com.example.playlistmaker.createPlaylist.data.mapper

import android.net.Uri
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

class PlaylistWithSongToPlaylistModelMapper(
    private val trackEntityToTrackMapper: TrackEntityToTrackMapper
) {
    fun execute(playlistWithSongs: PlaylistWithSongs?): PlaylistModel {
        if (playlistWithSongs != null) {
            var playlistCoverUri: Uri? = null
            if (playlistWithSongs.playlist.playlistCover.isNotEmpty()) {
                playlistCoverUri = Uri.parse(playlistWithSongs.playlist.playlistCover)
            }
            return PlaylistModel(playlistId = playlistWithSongs.playlist.playlistId,
                playlistName = playlistWithSongs.playlist.playlistName,
                playlistDescription = playlistWithSongs.playlist.playlistDescription,
                playlistCoverImage = playlistCoverUri,
                tracks = playlistWithSongs.tracks.map { trackEntity ->
                    trackEntityToTrackMapper.execute(trackEntity)
                })
        } else return PlaylistModel()
    }
}
