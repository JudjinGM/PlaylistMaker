package com.example.playlistmaker.library.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import kotlinx.coroutines.flow.Flow

interface GetPlaylistListUseCase {
    suspend fun execute(): Flow<List<PlaylistModel>>

    class Base(private val playListRepository: PlayListRepository) : GetPlaylistListUseCase {
        override suspend fun execute(): Flow<List<PlaylistModel>> {
            return playListRepository.getAllPlaylists()
        }
    }
}