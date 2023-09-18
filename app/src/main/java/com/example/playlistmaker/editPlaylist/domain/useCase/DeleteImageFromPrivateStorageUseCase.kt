package com.example.playlistmaker.editPlaylist.domain.useCase

import android.net.Uri
import com.example.playlistmaker.createPlaylist.domain.repository.ImageRepository

interface DeleteImageFromPrivateStorageUseCase {
    suspend fun execute(uri: Uri)
    class Base(private val imageRepository: ImageRepository) :
        DeleteImageFromPrivateStorageUseCase {
        override suspend fun execute(uri: Uri) {
            imageRepository.deleteImageFromPrivate(uri)
        }
    }
}