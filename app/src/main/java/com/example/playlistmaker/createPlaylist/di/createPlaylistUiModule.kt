package com.example.playlistmaker.createPlaylist.di

import com.example.playlistmaker.createPlaylist.ui.viewModel.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createPlaylistUiModule = module {

    viewModelOf(::CreatePlaylistViewModel)

}