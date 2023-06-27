package com.example.playlistmaker.audio_player.di

import android.media.MediaPlayer
import com.example.playlistmaker.audio_player.data.mediaplayer.MediaPlayerImpl
import com.example.playlistmaker.audio_player.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audio_player.domain.repository.NetworkConnectionProvider
import com.example.playlistmaker.audio_player.ui.setting.NetworkConnectionProviderImpl
import com.example.playlistmaker.audio_player.ui.view_model.AudioPlayerViewModel
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
            mediaPlayerControlInteractor = get(),
            isConnectedToNetworkUseCase = get(),
        )
    }
}