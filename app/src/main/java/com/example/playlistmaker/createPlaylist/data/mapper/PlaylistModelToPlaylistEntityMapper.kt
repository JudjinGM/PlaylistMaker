package com.example.playlistmaker.createPlaylist.data.mapper

import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

class PlaylistModelToPlaylistEntityMapper {
    fun execute(playlistModel: PlaylistModel): PlaylistEntity {
        return PlaylistEntity(
            playlistId = 0,
            playlistName = playlistModel.playlistName,
            playlistDescription = playlistModel.playlistDescription,
            playlistCover = playlistModel.playlistCoverImage.toString(),
        )
    }
}