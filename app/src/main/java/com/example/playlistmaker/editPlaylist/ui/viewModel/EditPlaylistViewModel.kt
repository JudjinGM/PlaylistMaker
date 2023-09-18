package com.example.playlistmaker.editPlaylist.ui.viewModel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.useCase.CreatePlaylistUseCase
import com.example.playlistmaker.createPlaylist.domain.useCase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.createPlaylist.ui.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.playlist.domain.useCase.GetPlaylistFlowUseCase
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    private val getPlaylistFlowUseCase: GetPlaylistFlowUseCase,
    createPlaylistUseCase: CreatePlaylistUseCase,
    saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase
) : CreatePlaylistViewModel(createPlaylistUseCase, saveImageToPrivateStorageUseCase) {

    init {
        viewModelScope.launch {
            getPlaylistFlowUseCase.execute(playlistId).collect {
                playlistName = it.playlistName
                playlistDescription = it.playlistDescription
                playlistCoverSavedToStorage = it.playlistCoverImage
            }
        }
    }

    fun setNameInput(): String {
        return playlistName
    }

    fun setNameDescription(): String {
        return playlistDescription
    }

    fun setCoverImage(): Uri? {
        return playlistCoverSavedToStorage
    }

}