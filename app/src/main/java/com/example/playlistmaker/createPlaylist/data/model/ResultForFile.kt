package com.example.playlistmaker.createPlaylist.data.model

import java.io.File

sealed interface ResultForFile {
    class Success(val file: File) : ResultForFile
    object Error : ResultForFile
}