package com.example.playlistmaker.createPlaylist.data

import android.net.Uri
import com.example.playlistmaker.createPlaylist.data.dataSource.ImagePrivateDataSource
import com.example.playlistmaker.createPlaylist.data.model.ResultForFile
import com.example.playlistmaker.createPlaylist.domain.repository.ImageRepository

class ImageRepositoryImpl(private val imagePrivateDataSource: ImagePrivateDataSource) :
    ImageRepository {
    override suspend fun saveImageToPrivate(uri: Uri): ResultForFile {
        return imagePrivateDataSource.saveToPrivateStorage(uri)
    }
}