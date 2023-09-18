package com.example.playlistmaker.editPlaylist.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository

interface GetPlaylistByIdUseCase {
    suspend fun execute(playlistId: Long): PlaylistModel
    class Base(private val playListRepository: PlayListRepository) : GetPlaylistByIdUseCase {
        override suspend fun execute(playlistId: Long): PlaylistModel {
            return playListRepository.getPlaylistById(playlistId)
        }
    }
}