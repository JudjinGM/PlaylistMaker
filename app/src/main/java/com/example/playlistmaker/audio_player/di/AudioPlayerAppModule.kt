package com.example.playlistmaker.audio_player.di

import android.media.MediaPlayer
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audio_player.domain.repository.NetworkConnectionProvider
import com.example.playlistmaker.audio_player.ui.mediaplayer.MediaPlayerImpl
import com.example.playlistmaker.audio_player.ui.setting.NetworkConnectionProviderImpl
import com.example.playlistmaker.audio_player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerAppModule = module {

    single<NetworkConnectionProvider> {
        NetworkConnectionProviderImpl(context = get())
    }

    factory<MediaPlayerContract> {
        MediaPlayerImpl(MediaPlayer())
    }

    viewModel<AudioPlayerViewModel> { (track: Track) ->
        AudioPlayerViewModel(
            track = track,
            mediaPlayerControlInteractor = get(),
            isConnectedToNetworkUseCase = get()
        )
    }
}