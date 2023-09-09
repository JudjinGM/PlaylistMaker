package com.example.playlistmaker.audioPlayer.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import com.example.playlistmaker.search.domain.model.Track

interface AddTrackToPlaylistUseCase {
    suspend fun execute(playlistId: Long, track: Track)

    class Base(private val repository: PlayListRepository) : AddTrackToPlaylistUseCase {
        override suspend fun execute(playlistId: Long, track: Track) {
            repository.addTrackToPlayList(playlistId, track)
        }
    }
}