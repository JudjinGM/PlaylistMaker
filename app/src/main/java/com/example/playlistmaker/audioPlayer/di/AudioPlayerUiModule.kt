package com.example.playlistmaker.audioPlayer.di

import android.media.MediaPlayer
import com.example.playlistmaker.audioPlayer.data.mediaplayer.MediaPlayerImpl
import com.example.playlistmaker.audioPlayer.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audioPlayer.domain.repository.NetworkConnectionProvider
import com.example.playlistmaker.audioPlayer.ui.setting.NetworkConnectionProviderImpl
import com.example.playlistmaker.audioPlayer.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerUiModule = module {

    single<NetworkConnectionProvider> {
        NetworkConnectionProviderImpl(context = get())
    }

    factory {
        MediaPlayer()
    }

    factory<MediaPlayerContract> {
        MediaPlayerImpl(mediaPlayer = get())
    }

    viewModel { (track: Track) ->
        AudioPlayerViewModel(
            track = track,
            mediaPlayerContract = get(),
            isConnectedToNetworkUseCase = get(),
            addTrackToFavoritesUseCase = get(),
            deleteTrackFromFavoritesUseCase = get(),
            getPlaylistListUseCase = get(),
            addTrackToPlaylistUseCase = get()
        )
    }
}