package com.example.playlistmaker.playlist.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import com.example.playlistmaker.search.domain.model.Track

interface DeleteTrackFromPlaylistUseCase {
    suspend fun execute(playlistId: Long, track: Track)

    class Base(private val playListRepository: PlayListRepository) :
        DeleteTrackFromPlaylistUseCase {
        override suspend fun execute(playlistId: Long, track: Track) {
            playListRepository.removeTrackFromPlaylist(playlistId, track)
        }

    }
}