package com.example.playlistmaker.editPlaylist.di

import com.example.playlistmaker.editPlaylist.ui.viewModel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val editPlaylistUiModule = module {

    viewModelOf(::EditPlaylistViewModel)
}