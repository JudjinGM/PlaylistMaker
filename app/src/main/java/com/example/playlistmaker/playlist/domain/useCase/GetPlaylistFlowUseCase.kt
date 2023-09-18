package com.example.playlistmaker.playlist.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import kotlinx.coroutines.flow.Flow

interface GetPlaylistFlowUseCase {
    suspend fun execute(playlistId: Long): Flow<PlaylistModel>

    class Base(private val playListRepository: PlayListRepository) : GetPlaylistFlowUseCase {
        override suspend fun execute(playlistId: Long): Flow<PlaylistModel> {
            return playListRepository.getPlaylistByIdFlow(playlistId)
        }
    }
}