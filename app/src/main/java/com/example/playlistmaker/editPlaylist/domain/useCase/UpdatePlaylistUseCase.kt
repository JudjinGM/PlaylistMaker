package com.example.playlistmaker.editPlaylist.domain.useCase

import android.net.Uri
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository
import com.example.playlistmaker.search.domain.model.Track

interface UpdatePlaylistUseCase {
    suspend fun execute(
        playlistId: Long,
        name: String,
        description: String,
        coverImage: Uri?,
        tracks: List<Track>
    )

    class Base(private val playListRepository: PlayListRepository) : UpdatePlaylistUseCase {
        override suspend fun execute(
            playlistId: Long,
            name: String,
            description: String,
            coverImage: Uri?,
            tracks: List<Track>
        ) {
            playListRepository.updatePlaylist(
                PlaylistModel(
                    playlistId = playlistId,
                    playlistName = name,
                    playlistDescription = description,
                    playlistCoverImage = coverImage,
                    tracks = tracks
                )
            )
        }
    }
}