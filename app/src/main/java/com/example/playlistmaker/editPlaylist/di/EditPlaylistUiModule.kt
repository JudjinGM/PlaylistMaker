package com.example.playlistmaker.editPlaylist.di

import com.example.playlistmaker.editPlaylist.ui.viewModel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editPlaylistUiModule = module {
    viewModel { (playlistId: Long) ->
        EditPlaylistViewModel(
            playlistId = playlistId,
            getPlaylistByIdUseCase = get(),
            createPlaylistUseCase = get(),
            saveImageToPrivateStorageUseCase = get(),
            updatePlaylistUseCase = get(),
            deleteImageFromPrivateStorageUseCase = get()
        )
    }
}