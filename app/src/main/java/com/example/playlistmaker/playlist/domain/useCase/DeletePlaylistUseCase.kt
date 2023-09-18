package com.example.playlistmaker.playlist.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository

interface DeletePlaylistUseCase {
    suspend fun execute(playlistId: Long, playlistModel: PlaylistModel)
    class Base(private val playListRepository: PlayListRepository) : DeletePlaylistUseCase {
        override suspend fun execute(playlistId: Long, playlistModel: PlaylistModel) {
            playListRepository.deletePlaylist(playlistId, playlistModel)
        }
    }

}