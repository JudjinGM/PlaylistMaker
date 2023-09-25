package com.example.playlistmaker.audioPlayer.di

import android.media.MediaPlayer
import com.example.playlistmaker.audioPlayer.data.mediaplayer.MediaPlayerImpl
import com.example.playlistmaker.audioPlayer.domain.repository.MediaPlayerContract
import com.example.playlistmaker.audioPlayer.domain.repository.NetworkConnectionProvider
import com.example.playlistmaker.audioPlayer.ui.setting.NetworkConnectionProviderImpl
import com.example.playlistmaker.audioPlayer.ui.viewModel.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val audioPlayerUiModule = module {

    singleOf(::NetworkConnectionProviderImpl) { bind<NetworkConnectionProvider>() }

    factoryOf(::MediaPlayer)

    factoryOf(::MediaPlayerImpl) { bind<MediaPlayerContract>() }

    viewModelOf(::AudioPlayerViewModel)

}