package com.example.playlistmaker.createPlaylist.domain.repository

import android.net.Uri
import com.example.playlistmaker.createPlaylist.data.model.ResultForFile

interface ImageRepository {
    suspend fun saveImageToPrivate(uri: Uri): ResultForFile
    suspend fun deleteImageFromPrivate(uri: Uri)
}
