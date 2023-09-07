package com.example.playlistmaker.createPlaylist.domain.useCase

import android.net.Uri
import android.util.Log
import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.createPlaylist.domain.repository.PlayListRepository

interface CreatePlaylistUseCase {
    suspend fun execute(name: String, description: String, coverImage: Uri?)
    class Base(private val playListRepository: PlayListRepository) : CreatePlaylistUseCase {
        override suspend fun execute(name: String, description: String, coverImage: Uri?) {
            Log.d("judjin_create_playlist_use_case", "$coverImage")
            playListRepository.createPlaylist(
                PlaylistModel(
                    playlistId = 0,
                    playlistName = name,
                    playlistDescription = description,
                    playlistCoverImage = coverImage,
                    tracks = emptyList()
                )
            )
        }
    }
}