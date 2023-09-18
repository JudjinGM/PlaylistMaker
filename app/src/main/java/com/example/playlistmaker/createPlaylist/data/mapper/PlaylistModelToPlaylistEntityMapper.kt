package com.example.playlistmaker.createPlaylist.data.mapper

import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

class PlaylistModelToPlaylistEntityMapper {
    fun execute(playlistModel: PlaylistModel, playlistId: Long = 0): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlistId,
            playlistName = playlistModel.playlistName,
            playlistDescription = playlistModel.playlistDescription,
            playlistCover = playlistModel.playlistCoverImage.toString(),
        )
    }
}