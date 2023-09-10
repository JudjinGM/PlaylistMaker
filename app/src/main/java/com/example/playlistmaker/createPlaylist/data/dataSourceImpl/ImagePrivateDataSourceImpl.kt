package com.example.playlistmaker.createPlaylist.data.dataSourceImpl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.createPlaylist.data.dataSource.ImagePrivateDataSource
import com.example.playlistmaker.createPlaylist.data.model.ResultForFile
import java.io.File
import java.io.FileOutputStream

class ImagePrivateDataSourceImpl(private val context: Context) : ImagePrivateDataSource {
    override suspend fun saveToPrivateStorage(uri: Uri): ResultForFile {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_PATH
        )
        if (!filePath.exists()) {
            filePath.mkdir()
        }

        val file = File(filePath, System.currentTimeMillis().toString() + COVER_IMAGE_FILE_NAME)
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    BitmapFactory.decodeStream(inputStream)
                        .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
                }
            }
            ResultForFile.Success(file)
        } catch (e: Exception) {
            ResultForFile.Error
        }
    }

    companion object {
        const val DIRECTORY_PATH = "playlist_covers"
        const val COVER_IMAGE_FILE_NAME = "playlist_cover.jpg"
    }
}