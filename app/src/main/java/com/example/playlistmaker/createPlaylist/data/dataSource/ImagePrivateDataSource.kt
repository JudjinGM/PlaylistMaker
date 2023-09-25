package com.example.playlistmaker.createPlaylist.data.dataSource

import android.net.Uri
import com.example.playlistmaker.createPlaylist.data.model.ResultForFile

interface ImagePrivateDataSource {
    suspend fun saveToPrivateStorage(uri: Uri): ResultForFile
    suspend fun deleteImageFromPrivateStorage(uri: Uri)

}