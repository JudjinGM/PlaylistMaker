package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val playlistUiModule = module {

    viewModelOf(::PlaylistViewModel)

}