package com.example.playlistmaker.createPlaylist.domain.useCase

import android.net.Uri
import com.example.playlistmaker.createPlaylist.data.model.ResultForFile
import com.example.playlistmaker.createPlaylist.domain.repository.ImageRepository

interface SaveImageToPrivateStorageUseCase {
    suspend fun execute(uri: Uri): ResultForFile

    class Base(private val imageRepository: ImageRepository) : SaveImageToPrivateStorageUseCase {
        override suspend fun execute(uri: Uri): ResultForFile {
            return imageRepository.saveImageToPrivate(uri)
        }
    }
}